package com.dolgih.idfTestTask.services;

import com.dolgih.idfTestTask.entities.ExchangeRate;
import com.dolgih.idfTestTask.repositories.ExchangeRateRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateService {
    private final RestTemplate restTemplate;
    private final ExchangeRateRepository exchangeRateRepository;

    @Value("${currency.api.url}")
    private String apiUrl;

    @Scheduled(cron = "${currency.cron}")
    public void updateDailyCurrencyRates() {
        log.info("Запрос новых курсов валют...");
        List<String> currencies = List.of("RUB", "KZT");

        ResponseEntity<JsonNode> response;
        try {
            response = restTemplate.exchange(apiUrl, HttpMethod.GET, null, JsonNode.class);
        } catch (RestClientException e) {
            log.error("Ошибка при запросе курсов валют: {}", e.getMessage());
            return;
        }

        if (response.getBody() == null) {
            log.warn("Ответ API пуст или недоступен, курсы не обновлены.");
            return;
        }
        JsonNode ratesNode = response.getBody().path("rates");
        if (ratesNode.isMissingNode()) {
            log.warn("Ответ API не содержит ключ 'rates', курсы не обновлены.");
            return;
        }

        for (String currency : currencies) {
            Double rate = extractExchangeRateFromRatesNode(ratesNode, currency);
            if (rate == null) {
                continue;
            }

            ExchangeRate exchangeRate = ExchangeRate.builder()
                                                    .rate(BigDecimal.valueOf(rate))
                                                    .currencyPair(currency + "/USD")
                                                    .build();

            exchangeRateRepository.save(exchangeRate);
            log.info("Обновлён курс валют: {}/USD - {}", currency, rate);
        }
    }

    public Double extractExchangeRateFromRatesNode(JsonNode rates, String currency) {
        JsonNode currencyNode = rates.path(currency);
        if (currencyNode.isMissingNode() || !currencyNode.isNumber()) {
            log.warn("Курс валюты для {} отсутствует или имеет некорректный формат", currency);
            return null;
        }

        return currencyNode.asDouble();
    }

    public Optional<ExchangeRate> getLatestExchangeRate(String currency) {
        String currencyPair = currency + "/USD";
        Optional<ExchangeRate> latestRate =
                exchangeRateRepository.findCurrentExchangeRate(currencyPair);

        if (latestRate.isPresent()) {
            log.info("Курс транзакции: {} - {}", currencyPair, latestRate.get().getRate());
            return latestRate;
        }

        log.warn("В БД курс для {} отсутствует. Запрашиваем курс у внешнего API...", currencyPair);
        updateDailyCurrencyRates();

        return exchangeRateRepository.findCurrentExchangeRate(currencyPair);
    }
}
