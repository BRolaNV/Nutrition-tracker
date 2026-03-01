package com.rolan.service;

import com.rolan.dao.UserTargetDAO;
import com.rolan.model.User;
import com.rolan.model.UserTargets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class UserTargetService {
    @Autowired
    UserTargetDAO userTargetDAO;

    public UserTargets createUserTargets
            (User user, double protein, double fat, double carbohydrates, double fiber){
        UserTargets userTargets = null;
        try {
            userTargets = new UserTargets(user.getId(), protein, fat, carbohydrates, fiber);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return userTargets;
    }

    public boolean existsByUserId(User user){
        try {
            return userTargetDAO.existsByUserId(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UserTargets findTargetsByUserId(User user) {
        try {
            return userTargetDAO.findTargetsByUserId(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
