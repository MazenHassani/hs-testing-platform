package com.harborspace.spacecraft.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@OpenAPIDefinition(
        info = @Info(
                title = "Harborspace API",
                version = "v1")
)
@Configuration
@EnableAsync
@EnableScheduling
public class AppConfig {
    private static int connectionTimeoutMs = 30000;

    @Bean
    RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(connectionTimeoutMs);
        clientHttpRequestFactory.setReadTimeout(connectionTimeoutMs);
        return new RestTemplate(clientHttpRequestFactory);
    }
}
