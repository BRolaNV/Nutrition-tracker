package com.rolan.service;

import com.rolan.model.MealEntry;
import com.rolan.model.User;

import java.util.List;

public interface MealEntryService {
    void createMealEntry(User user, double protein, double fat, double carbohydrates, double fiber);
    List<MealEntry> getMealEntry(User user);
}
