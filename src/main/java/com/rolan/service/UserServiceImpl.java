package com.rolan.service;

import com.rolan.model.User;
import com.rolan.repository.UserRepository;
import com.rolan.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void createUser(String name, Long chatId) {
        userRepository.save(User.builder()
                .userName(name).
                chatId(chatId).
                build());
    }

    @Override
    public boolean existByChatId(Long chatId) {
        return userRepository.existsByChatId(chatId);
    }

    @Override
    public User getUser(Long chatId) {
        return userRepository.findByChatId(chatId);
    }
}
