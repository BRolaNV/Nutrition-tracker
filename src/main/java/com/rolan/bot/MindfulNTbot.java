package com.rolan.bot;

import com.rolan.model.User;
import com.rolan.model.UserState;
import com.rolan.model.UserTargets;
import com.rolan.service.UserService;
import com.rolan.service.UserTargetService;
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
    HashMap<Long, double[]> usersTargets = new HashMap<>();

    @Autowired
    private UserService userService;
    @Autowired
    private UserTargetService userTargetService;

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

    public static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) return false;
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
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
                    send(sendMessage);

                        if(!userTargetService.existsByUserId(userService.getUser(chatId))){
                            sendMessage.setChatId(chatId);
                            sendMessage.setText("What's your target protein?");
                            userService.createUser(message, chatId);
                            usersStates.put(chatId, UserState.WAITING_FOR_PROTEIN);
                            send(sendMessage);
                        }

                } else {
                    sendMessage.setChatId(chatId);
                    sendMessage.setText("Hello, what's your name?");
                    usersStates.put(chatId, UserState.WAITING_FOR_NAME);
                    send(sendMessage);
                }


            } else {

                switch (usersStates.get(chatId)) {

                    case WAITING_FOR_NAME:

                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Nice to meet you " + message + "!" +
                                "\nWhat's your target protein?");
                        userService.createUser(message, chatId);
                        usersStates.put(chatId, UserState.WAITING_FOR_PROTEIN);
                        send(sendMessage);
                        break;

                    case WAITING_FOR_PROTEIN:

                    if (isNumeric(message)) {
                        usersTargets.put(chatId, new double[4]);
                        usersTargets.get(chatId)[0] = Double.parseDouble(message);
                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Grate!" +
                                "\nWhat's your target fat?");
                        usersStates.put(chatId, UserState.WAITING_FOR_FAT);
                        send(sendMessage);
                    } else {
                        sendMessage.setChatId(chatId);
                        sendMessage.setText("i'm so sorry, but this isn't numeric" +
                                "\nPlease try again!");
                        usersStates.put(chatId, UserState.WAITING_FOR_PROTEIN);
                        send(sendMessage);
                    }
                    break;

                    case WAITING_FOR_FAT:

                        if (isNumeric(message)) {
                            usersTargets.get(chatId)[1] = Double.parseDouble(message);
                            sendMessage.setChatId(chatId);
                            sendMessage.setText("Excellent!" +
                                    "\nWhat's your target carbs?");
                            usersStates.put(chatId, UserState.WAITING_FOR_CARBS);
                            send(sendMessage);
                        } else {
                            sendMessage.setChatId(chatId);
                            sendMessage.setText("i'm so sorry, but this isn't numeric" +
                                    "\nPlease try again!");
                            usersStates.put(chatId, UserState.WAITING_FOR_FAT);
                            send(sendMessage);
                        }
                        break;

                    case WAITING_FOR_CARBS:

                        if (isNumeric(message)) {
                            usersTargets.get(chatId)[2] = Double.parseDouble(message);
                            sendMessage.setChatId(chatId);
                            sendMessage.setText("Good job!" +
                                    "\nWhat's your target fiber?");
                            usersStates.put(chatId, UserState.WAITING_FOR_FIBER);
                            send(sendMessage);
                        } else {
                            sendMessage.setChatId(chatId);
                            sendMessage.setText("i'm so sorry, but this isn't numeric" +
                                    "\nPlease try again!");
                            usersStates.put(chatId, UserState.WAITING_FOR_CARBS);
                            send(sendMessage);
                        }
                        break;

                    case WAITING_FOR_FIBER:

                        if (isNumeric(message)) {
                            usersTargets.get(chatId)[3] = Double.parseDouble(message);

                            double[] targets = usersTargets.get(chatId);
                            double protein = targets[0];
                            double fat  = targets[1];
                            double carbohydrates = targets[2];
                            double fiber = targets[3];

                            UserTargets userTargets = userTargetService.createUserTargets
                                    (userService.getUser(chatId), protein, fat, carbohydrates, fiber);

                            sendMessage.setChatId(chatId);
                            sendMessage.setText("It's done, congratulations!" +
                                    "\nIt's your targets:" +  userTargets.toString());
                            send(sendMessage);
                        } else {
                            sendMessage.setChatId(chatId);
                            sendMessage.setText("i'm so sorry, but this isn't numeric" +
                                    "\nPlease try again!");
                            usersStates.put(chatId, UserState.WAITING_FOR_FIBER);
                            send(sendMessage);
                        }
                };



            }
        }
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }
}
