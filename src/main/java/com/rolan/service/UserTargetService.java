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

    public UserTargets getOrCreateUserTargets
            (User user, double protein, double fat, double carbohydrates, double fiber) throws SQLException {
        UserTargets userTargets;
        if (userTargetDAO.existsByUserId(user)){
           userTargets = userTargetDAO.findTargetsByUserId(user);
        } else {
            userTargets = new UserTargets(user.getId(), protein, fat, carbohydrates, fiber);
            userTargetDAO.saveTargets(userTargets);
        }
        return userTargets;
    }
}
