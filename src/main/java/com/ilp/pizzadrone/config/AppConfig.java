package com.ilp.pizzadrone.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    /**
     * Create a RestTemplate bean
     * @return a RestTemplate object
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
