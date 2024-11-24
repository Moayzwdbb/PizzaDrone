package com.ilp.pizzadrone.service;

import com.ilp.pizzadrone.dto.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class TestOrderCasesService {
    private static final String ORDERS_URL = "https://ilp-rest-2024.azurewebsites.net/orders";
    private final RestTemplate restTemplate;

    public TestOrderCasesService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Order> fetchOrders() {
        Order[] orders = restTemplate.getForObject(ORDERS_URL, Order[].class);
        assert orders != null : "The fetched orders should not be null";
        return List.of(orders);
    }
}
