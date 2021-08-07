package ru.otus.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    @Value("${rest-template.config.connectTimeout:300000}")
    private int connectTimeout;
    @Value("${rest-template.config.readTimeout:300000}")
    private int readTimeout;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder)
    {
        return restTemplateBuilder
           .setConnectTimeout(Duration.ofMillis(connectTimeout))
           .setReadTimeout(Duration.ofMillis(readTimeout))
           .build();
    }
}