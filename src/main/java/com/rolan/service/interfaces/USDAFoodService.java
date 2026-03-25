package com.rolan.service.interfaces;

import com.rolan.model.usda.USDAFood;
import com.rolan.model.usda.USDAFoodSearchResponse;

public interface USDAFoodService {
    USDAFoodSearchResponse searchFood(String query, int pageSize);
    USDAFood getFoodById(Long fdcId);
}
