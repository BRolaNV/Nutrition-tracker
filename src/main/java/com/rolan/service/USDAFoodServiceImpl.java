package com.rolan.service;

import com.rolan.model.usda.USDAFood;
import com.rolan.model.usda.USDAFoodSearchResponse;
import com.rolan.service.interfaces.USDAFoodService;
import org.jvnet.hk2.internal.Collector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class USDAFoodServiceImpl implements USDAFoodService {

    private final WebClient webClient;
    private final String apiKey;

    public USDAFoodServiceImpl(@Value("${usda.api.key}") String apiKey,
                               @Value("${usda.api.url}") String apiUrl) {
        this.apiKey = apiKey;
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(1024 * 1024))
                .build();
    }

    @Override
    public USDAFoodSearchResponse searchFood(String query, int pageSize) {

        USDAFoodSearchResponse usdaFoodSearchResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/foods/search")
                        .queryParam("query", query)
                        .queryParam("api_key", apiKey)
                        .queryParam("pageSize", pageSize)
                        .queryParam("dataType", "Foundation,SR Legacy")
                        .build())
                .retrieve()
                .bodyToMono(USDAFoodSearchResponse.class)
                .block();

        Set<Integer> needed = Set.of(1003, 1004, 1005, 1079);

        usdaFoodSearchResponse.getFoods().forEach(x -> {
            x.setFoodNutrients(
                    x.getFoodNutrients().stream()
                            .filter(nutrient -> needed.contains(nutrient.getNutrientId()))
                            .collect(Collectors.toList())
            );
        });

        return usdaFoodSearchResponse;
    }

    @Override
    public USDAFood getFoodById(Long fdcId) {
        return null;
    }
}
