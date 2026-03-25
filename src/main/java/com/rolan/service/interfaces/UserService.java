package com.rolan.service.interfaces;


import com.rolan.model.User;

public interface UserService {
    void createUser(String name, Long chatId);
    boolean existByChatId(Long chatId);
    User getUser(Long chatId);
}
