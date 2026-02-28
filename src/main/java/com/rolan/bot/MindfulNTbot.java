package com.rolan.bot;

import com.rolan.model.User;
import com.rolan.model.UserState;
import com.rolan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;

@Component
public class MindfulNTbot extends TelegramLongPollingBot {

    HashMap<Long, UserState> usersStates = new HashMap<>();

    @Autowired
    private UserService userService;

    @Value("${telegram.bot.username}")
    private String botUserName;


    public MindfulNTbot(@Value("${telegram.bot.token}") String token) {
        super(token);
    }

    protected void send(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {

            String message = update.getMessage().getText();
            SendMessage sendMessage = new SendMessage();
            Long chatId = update.getMessage().getChatId();

            if (message.equals("/start")) {

                if (userService.existByChatId(chatId)){
                    User user = userService.getUser(chatId);
                    sendMessage.setChatId(chatId);
                    sendMessage.setText("Hello, " + user.getUserName() + "!");
                } else {
                    sendMessage.setChatId(chatId);
                    sendMessage.setText("Hello, what's your name?");
                    usersStates.put(chatId, UserState.WAITING_FOR_NAME);
                    send(sendMessage);
                }


            } else if (usersStates.get(chatId) == UserState.WAITING_FOR_NAME) {

                sendMessage.setChatId(chatId);
                sendMessage.setText("Nice to meet you " + message + "!" +
                        "\nWhat's your target protein?");
                userService.createUser(message, chatId);
                usersStates.put(chatId, UserState.WAITING_FOR_PROTEIN);
                send(sendMessage);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }
}
