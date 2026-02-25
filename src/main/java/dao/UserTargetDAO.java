package dao;

import model.User;
import model.UserTargets;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;


@Repository
public class UserTargetDAO {

    public void saveTargets(UserTargets userTargets) throws SQLException {

        String sql ="INSERT INTO user_targets (user_id, protein, fat, carbohydrates, fiber, date) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {

            preparedStatement.setInt(1, userTargets.getUserId());
            preparedStatement.setDouble(2, userTargets.getProtein());
            preparedStatement.setDouble(3, userTargets.getFat());
            preparedStatement.setDouble(4, userTargets.getCarbohydrates());
            preparedStatement.setDouble(5, userTargets.getFiber());
            preparedStatement.setString(6, String.valueOf(LocalDate.now()));
            preparedStatement.execute();

        }
    }

    public UserTargets findTargetsByUserId(User user) throws SQLException{
        String sql = "SELECT * FROM user_targets WHERE user_id = ? ORDER BY id DESC LIMIT 1";
        UserTargets userTargets;

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {

            preparedStatement.setInt(1, user.getId());
            ResultSet set = preparedStatement.executeQuery();

            if (!set.next()){
                throw new SQLException("Targets not found");
            }

            int userId = set.getInt(2);
            double protein = set.getDouble(3);
            double fat = set.getDouble(4);
            double carbohydrates = set.getDouble(5);
            double fiber = set.getDouble(6);
            String date = set.getString(7);

            userTargets = new UserTargets(userId, protein, fat, carbohydrates, fiber);
            userTargets.setDate(date);
        }
        return  userTargets;
    }

    public boolean existsByUserId(User user) throws SQLException{
        String sql = "SELECT * FROM user_targets WHERE user_id = ?";
        boolean isExist;

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {

            preparedStatement.setInt(1, user.getId());
            ResultSet set = preparedStatement.executeQuery();

            isExist = set.next();
        }
        return  isExist;
    }
}
