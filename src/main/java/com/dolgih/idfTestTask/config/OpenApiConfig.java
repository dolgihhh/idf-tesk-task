package com.dolgih.idfTestTask.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Spending Limit Tracker API",
                description = "Financial monitoring service that helps track and control customer " +
                              "spending across multiple currencies.",
                version = "0.0.1",
                contact = @Contact(name = "Dolgih Pavel", email = "dolgihpv@mail.ru")
        )
)
public class OpenApiConfig {
}
