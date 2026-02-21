package dao;

import model.MealEntry;
import model.User;
import model.UserTargets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MealEntryDAO {
    public static void saveMealEntry(MealEntry mealEntry) throws SQLException {

        String sql ="INSERT INTO meal_entries (user_id, name, protein, fat, carbohydrates, fiber, date) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {

            preparedStatement.setInt(1, mealEntry.getUserId());
            preparedStatement.setString(2, mealEntry.getNameOfMeal());
            preparedStatement.setDouble(3, mealEntry.getProtein());
            preparedStatement.setDouble(4, mealEntry.getFat());
            preparedStatement.setDouble(5, mealEntry.getCarbohydrates());
            preparedStatement.setDouble(6, mealEntry.getFiber());
            preparedStatement.setString(7, String.valueOf(LocalDate.now()));
            preparedStatement.execute();

        }
    }

    public static List<MealEntry> findMealEntriesByUserId(User user) throws SQLException{
        String sql = "SELECT * FROM meal_entries WHERE user_id = ? AND date = ?";
        List<MealEntry> mealEntries = new ArrayList<>();

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {

            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, String.valueOf(LocalDate.now()));
            ResultSet set = preparedStatement.executeQuery();

            while (set.next()){
                int userId = set.getInt(2);
                String name = set.getString(3);
                double protein = set.getDouble(4);
                double fat = set.getDouble(5);
                double carbohydrates = set.getDouble(6);
                double fiber = set.getDouble(7);
                String date = set.getString(8);


                MealEntry mealEntry = new MealEntry(userId, name, protein, fat, carbohydrates, fiber);
                mealEntry.setDate(date);
                mealEntries.add(mealEntry);

            }
        }
        return mealEntries;
    }
}
