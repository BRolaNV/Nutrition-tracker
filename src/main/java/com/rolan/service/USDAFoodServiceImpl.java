package com.rolan.service;

import com.rolan.model.usda.USDAFood;
import com.rolan.model.usda.USDAFoodSearchResponse;
import com.rolan.service.interfaces.USDAFoodService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class USDAFoodServiceImpl implements USDAFoodService {

    private final WebClient webClient;
    private final String apiKey;

    public USDAFoodServiceImpl(@Value("${usda.api.key}") String apiKey,
                               @Value("${usda.api.url}") String apiUrl) {
        this.apiKey = apiKey;
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .build();
    }

    @Override
    public USDAFoodSearchResponse searchFood(String query, int pageSize) {
        return null;
    }

    @Override
    public USDAFood getFoodById(Long fdcId) {
        return null;
    }
}
