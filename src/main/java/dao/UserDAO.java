package dao;

import model.User;

import java.sql.*;

public class UserDAO {

    public static int saveUser(String name) throws SQLException {
        int id = 0;
        String sql = "INSERT INTO users (name) VALUES (?)";
        String idSql = "SELECT last_insert_rowid()";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             PreparedStatement preparedStatement1 = connection.prepareStatement(idSql);) {

            preparedStatement.setString(1, name);
            preparedStatement.execute();

            ResultSet set = preparedStatement1.executeQuery();
            set.next();

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

            if (!set.next()){
                throw new SQLException("User not found");
            }

            int userId = set.getInt(1);
            String userName = set.getString(2);

            user = new User(userName, userId);
        }
        return  user;
    }

    public static boolean existsByName(String name) throws SQLException{
        String sql = "SELECT * FROM users WHERE name = ?";
        boolean isExist;

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {

            preparedStatement.setString(1, name);
            ResultSet set = preparedStatement.executeQuery();

            isExist = set.next();
        }
        return  isExist;
    }
}
