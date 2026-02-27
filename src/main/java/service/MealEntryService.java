package service;


import dao.MealEntryDAO;
import dao.UserTargetDAO;
import model.MealEntry;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class MealEntryService {
    @Autowired
    private MealEntryDAO mealEntryDAO;

    public void createMealEntry(User user, String nameOfMeal, double protein, double fat, double carbohydrates, double fiber) throws SQLException {
        mealEntryDAO.saveMealEntry(new MealEntry(user.getId(), nameOfMeal, protein, fat,  carbohydrates, fiber));
    }

    public List<MealEntry> getMealEntry(User user) throws SQLException {
        return mealEntryDAO.findMealEntriesByUserId(user);
    }
}
