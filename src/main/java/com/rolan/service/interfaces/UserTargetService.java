package com.rolan.service.interfaces;

import com.rolan.model.User;
import com.rolan.model.UserTargets;

import java.util.List;

public interface UserTargetService {
    UserTargets createUserTargets(User user, double protein, double fat, double carbohydrates, double fiber);
    boolean existsByUserId(User user);
    List<UserTargets> findTargetsByUserId(User user);
}
