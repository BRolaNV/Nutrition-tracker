package com.rolan.bot;

import com.rolan.model.MealEntry;
import com.rolan.model.User;
import com.rolan.model.UserState;
import com.rolan.model.UserTargets;
import com.rolan.service.MealEntryService;
import com.rolan.service.UserService;
import com.rolan.service.UserTargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class MindfulNTbot extends TelegramLongPollingBot {

    HashMap<Long, UserState> usersStates = new HashMap<>();
    HashMap<Long, double[]> usersMacros = new HashMap<>();
    HashMap<Long, String> usersContext = new HashMap<>();

    @Autowired
    private UserService userService;
    @Autowired
    private UserTargetService userTargetService;
    @Autowired
    private MealEntryService mealEntryService;

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
                    sendMessage = SendMessage.builder()
                            .chatId(chatId)
                            .text("Hello, " + user.getUserName() + "!")
                            .build();
                    send(sendMessage);
                    usersStates.put(chatId, UserState.MAIN_MENU);

                        if(!userTargetService.existsByUserId(userService.getUser(chatId))){
                            sendMessage = SendMessage.builder()
                                    .chatId(chatId)
                                    .text("What's your target protein?")
                                    .build();
                            usersStates.put(chatId, UserState.WAITING_FOR_PROTEIN);
                            usersContext.put(chatId, "target");
                            send(sendMessage);
                        } else {
                            sendMainMenu(chatId);
                        }

                } else {
                    sendMessage = SendMessage.builder()
                            .chatId(chatId)
                            .text("Hello, what's your name?")
                            .build();
                    usersStates.put(chatId, UserState.WAITING_FOR_NAME);
                    send(sendMessage);
                }

            } else {

                switch (usersStates.get(chatId)) {

                    case WAITING_FOR_NAME:

                        sendMessage = SendMessage.builder()
                                .chatId(chatId)
                                .text("Nice to meet you " + message + "!" +
                                        "\nWhat's your target protein?")
                                .build();
                        userService.createUser(message, chatId);
                        usersStates.put(chatId, UserState.WAITING_FOR_PROTEIN);
                        usersContext.put(chatId, "target");
                        send(sendMessage);
                        break;

                    case WAITING_FOR_PROTEIN:

                    if (isNumeric(message)) {
                        usersMacros.put(chatId, new double[4]);
                        usersMacros.get(chatId)[0] = Double.parseDouble(message);
                        sendMessage = SendMessage.builder()
                                .chatId(chatId)
                                .text("Great!" +
                                        "\nWhat's about fat?")
                                .build();
                        usersStates.put(chatId, UserState.WAITING_FOR_FAT);
                        send(sendMessage);
                    } else {
                        sendMessage = SendMessage.builder()
                                .chatId(chatId)
                                .text("Please enter a number!")
                                .build();
                        usersStates.put(chatId, UserState.WAITING_FOR_PROTEIN);
                        send(sendMessage);
                    }
                    break;

                    case WAITING_FOR_FAT:

                        if (isNumeric(message)) {
                            usersMacros.get(chatId)[1] = Double.parseDouble(message);
                            sendMessage = SendMessage.builder()
                                    .chatId(chatId)
                                    .text("Excellent!" +
                                            "\nSome carbs?")
                                    .build();
                            usersStates.put(chatId, UserState.WAITING_FOR_CARBS);
                            send(sendMessage);
                        } else {
                            sendMessage = SendMessage.builder()
                                    .chatId(chatId)
                                    .text("Please enter a number!")
                                    .build();
                            usersStates.put(chatId, UserState.WAITING_FOR_FAT);
                            send(sendMessage);
                        }
                        break;

                    case WAITING_FOR_CARBS:

                        if (isNumeric(message)) {
                            usersMacros.get(chatId)[2] = Double.parseDouble(message);
                            sendMessage = SendMessage.builder()
                                    .chatId(chatId)
                                    .text("Good job!" +
                                            "\nAnd fiber for last.")
                                    .build();
                            usersStates.put(chatId, UserState.WAITING_FOR_FIBER);
                            send(sendMessage);
                        } else {
                            sendMessage = SendMessage.builder()
                                    .chatId(chatId)
                                    .text("Please enter a number!")
                                    .build();
                            usersStates.put(chatId, UserState.WAITING_FOR_CARBS);
                            send(sendMessage);
                        }
                        break;

                    case WAITING_FOR_FIBER:

                        if (isNumeric(message)) {

                            usersMacros.get(chatId)[3] = Double.parseDouble(message);

                            double[] targets = usersMacros.get(chatId);
                            double protein = targets[0];
                            double fat  = targets[1];
                            double carbohydrates = targets[2];
                            double fiber = targets[3];

                            if (usersContext.get(chatId).equals("target")) {

                                UserTargets userTargets = userTargetService.createUserTargets
                                        (userService.getUser(chatId), protein, fat, carbohydrates, fiber);

                                sendMessage = SendMessage.builder()
                                        .chatId(chatId)
                                        .text("It's done, congratulations!\n" +
                                                "\nYour targets:\n" + userTargets.toString())
                                        .build();
                                send(sendMessage);
                                sendMainMenu(chatId);
                                usersStates.put(chatId, UserState.MAIN_MENU);
                                usersMacros.remove(chatId);
                                usersContext.remove(chatId);

                            } else if (usersContext.get(chatId).equals("meal")) {

                                mealEntryService.createMealEntry(userService.getUser(chatId), protein, fat, carbohydrates, fiber);
                                sendMessage = SendMessage.builder()
                                        .chatId(chatId)
                                        .text("It's done!")
                                        .build();
                                send(sendMessage);
                                sendMainMenu(chatId);
                                usersStates.put(chatId, UserState.MAIN_MENU);
                                usersMacros.remove(chatId);
                                usersContext.remove(chatId);

                            }
                        } else {
                            sendMessage = SendMessage.builder()
                                    .chatId(chatId)
                                    .text("Please enter a number!")
                                    .build();
                            usersStates.put(chatId, UserState.WAITING_FOR_FIBER);
                            send(sendMessage);
                        }
                        break;

                    case MAIN_MENU:

                        if (message.equals("Add meal")) {

                            usersContext.put(chatId, "meal");
                            sendMessage = SendMessage.builder()
                                    .chatId(chatId)
                                    .text("How many macros did you eat?" +
                                            "\nLet's start with protein.")
                                    .build();
                            usersStates.put(chatId, UserState.WAITING_FOR_PROTEIN);

                            send(sendMessage);

                        } else if (message.equals("Get day result")) {

                            List<MealEntry> mealEntries = mealEntryService.getMealEntry(userService.getUser(chatId));

                            double protein = 0;
                            double fat = 0;
                            double carbohydrates = 0;
                            double fiber = 0;
                            double calories = 0;

                            UserTargets ut = userTargetService.findTargetsByUserId(userService.getUser(chatId));

                            for (MealEntry mealEntry : mealEntries) {
                                protein += mealEntry.getProtein();
                                fat += mealEntry.getFat();
                                carbohydrates += mealEntry.getCarbohydrates();
                                fiber += mealEntry.getFiber();
                                calories += mealEntry.getCalories();
                            }

                            sendMessage = SendMessage.builder()
                                    .chatId(chatId)
                                    .text("Today you've eaten: \n" +

                                            "\nProtein - " + String.format("%.2f", protein) +
                                            "\nFat - " + String.format("%.2f", fat) +
                                            "\nCarbs - " + String.format("%.2f", carbohydrates) +
                                            "\nFiber - " + String.format("%.2f", fiber) +
                                            "\nCalories - " + String.format("%.2f", calories) +

                                            "\n\nRemaining for today: \n" +

                                            "\nProtein - " + String.format("%.2f", (ut.getProtein() - protein)) +
                                            "\nFat - " + String.format("%.2f", (ut.getFat() - fat)) +
                                            "\nCarbs - " + String.format("%.2f", (ut.getCarbohydrates() - carbohydrates)) +
                                            "\nFiber - " + String.format("%.2f", (ut.getFiber() - fiber)) +
                                            "\nCalories - " + String.format("%.2f", (ut.getCalories() - calories)))
                                    .build();
                            send(sendMessage);
                        }
                        break;
                };
            }
        }
    }

    public void sendMainMenu(Long chatId){
        KeyboardRow row = new KeyboardRow();
        row.add("Add meal");
        row.add("Get day result");

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row);

        ReplyKeyboardMarkup replyMarkup = new ReplyKeyboardMarkup();
        replyMarkup.setKeyboard(keyboard);
        replyMarkup.setResizeKeyboard(true);
        replyMarkup.setOneTimeKeyboard(false);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Main menu:");
        message.setReplyMarkup(replyMarkup);
        send(message);
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }
}
