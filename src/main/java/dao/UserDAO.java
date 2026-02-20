package dao;

import model.User;

import java.sql.*;

public class UserDAO {

    public static int saveUser(String name) throws SQLException {
        int id = 0;
        String sql = "INSERT INTO users (name) VALUES (?)";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            preparedStatement.setString(1, name);
            preparedStatement.execute();

            ResultSet set = preparedStatement.getGeneratedKeys();

            if (!set.next()){
                throw new SQLException("User not found");
            }
            id = set.getInt(1);
        }
        return id;
    }

    public static User findByName(String name) throws SQLException{
        String sql = "SELECT * FROM users WHERE name = ?";
        User user;

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {

            preparedStatement.setString(1, name);
            ResultSet set = preparedStatement.executeQuery();
            set.next();

            int userId = set.getInt(1);
            String userName = set.getString(2);

            user = new User(userName, userId);
        }
        return  user;
    }
}
