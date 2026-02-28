package com.rolan.dao;

import com.rolan.model.User;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class UserDAO {

    public int saveUser(String name, Long chatId) throws SQLException {
        int id = 0;
        String sql = "INSERT INTO users (name, chatId) VALUES (?, ?)";
        String idSql = "SELECT last_insert_rowid()";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             PreparedStatement preparedStatement1 = connection.prepareStatement(idSql);) {

            preparedStatement.setString(1, name);
            preparedStatement.setLong(2, chatId);
            preparedStatement.execute();

            ResultSet set = preparedStatement1.executeQuery();
            set.next();

            id = set.getInt(1);
        }
        return id;
    }

    public User findByChatId(Long chatId) throws SQLException{
        String sql = "SELECT * FROM users WHERE chatId = ?";
        User user;

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {

            preparedStatement.setLong(1, chatId);
            ResultSet set = preparedStatement.executeQuery();

            if (!set.next()){
                throw new SQLException("User not found");
            }

            int userId = set.getInt(1);
            String userName = set.getString(2);
            Long userChatId = set.getLong(3);

            user = new User(userName, userId, userChatId);
        }
        return  user;
    }

    public boolean existsByChatId(Long chatId) throws SQLException{
        String sql = "SELECT * FROM users WHERE chatId = ?";
        boolean isExist;

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {

            preparedStatement.setLong(1, chatId);
            ResultSet set = preparedStatement.executeQuery();

            isExist = set.next();
        }
        return  isExist;
    }
}
