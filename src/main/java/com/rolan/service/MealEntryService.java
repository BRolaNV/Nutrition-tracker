package com.rolan.service;


import com.rolan.dao.MealEntryDAO;
import com.rolan.model.MealEntry;
import com.rolan.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class MealEntryService {
    @Autowired
    private MealEntryDAO mealEntryDAO;

    public void createMealEntry(User user, double protein, double fat, double carbohydrates, double fiber){
        try {
            mealEntryDAO.saveMealEntry(new MealEntry(user.getId(), protein, fat,  carbohydrates, fiber));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<MealEntry> getMealEntry(User user) {
        try {
            return mealEntryDAO.findMealEntriesByUserId(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
