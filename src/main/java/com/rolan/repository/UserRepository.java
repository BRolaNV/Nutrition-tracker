package com.rolan.repository;

import com.rolan.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByChatId(Long chatId);
    Boolean existsByChatId(Long chatId);
}
