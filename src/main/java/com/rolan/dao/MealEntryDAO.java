package com.rolan.dao;

import com.rolan.model.MealEntry;
import com.rolan.model.User;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MealEntryDAO {
    public void saveMealEntry(MealEntry mealEntry) throws SQLException {

        String sql ="INSERT INTO meal_entries (user_id, protein, fat, carbohydrates, fiber, date) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {

            preparedStatement.setInt(1, mealEntry.getUserId());
            preparedStatement.setDouble(2, mealEntry.getProtein());
            preparedStatement.setDouble(3, mealEntry.getFat());
            preparedStatement.setDouble(4, mealEntry.getCarbohydrates());
            preparedStatement.setDouble(5, mealEntry.getFiber());
            preparedStatement.setString(6, String.valueOf(LocalDate.now()));
            preparedStatement.execute();

        }
    }

    public List<MealEntry> findMealEntriesByUserId(User user) throws SQLException{
        String sql = "SELECT * FROM meal_entries WHERE user_id = ? AND date = ?";
        List<MealEntry> mealEntries = new ArrayList<>();

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {

            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, String.valueOf(LocalDate.now()));
            ResultSet set = preparedStatement.executeQuery();

            while (set.next()){
                int userId = set.getInt(2);
                double protein = set.getDouble(3);
                double fat = set.getDouble(4);
                double carbohydrates = set.getDouble(5);
                double fiber = set.getDouble(6);
                String date = set.getString(7);


                MealEntry mealEntry = new MealEntry(userId, protein, fat, carbohydrates, fiber);
                mealEntry.setDate(date);
                mealEntries.add(mealEntry);

            }
        }
        return mealEntries;
    }
}
