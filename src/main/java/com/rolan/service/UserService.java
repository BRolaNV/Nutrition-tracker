package com.rolan.service;

import com.rolan.dao.UserDAO;
import com.rolan.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

    public void createUser(String name, Long chatId) {
        try {
            User user = new User(name, userDAO.saveUser(name, chatId), chatId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existByChatId(Long chatId) {
        try {
            return userDAO.existsByChatId(chatId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUser(Long chatId) {
        try {
            return userDAO.findByChatId(chatId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
