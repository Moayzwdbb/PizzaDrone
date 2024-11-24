package com.ilp.pizzadrone.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    /**
     * Creates a new REST template bean
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
