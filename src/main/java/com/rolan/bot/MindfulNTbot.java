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
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
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

    protected void edit(EditMessageText message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) return false;
        try {
            if (str.contains(",")) {
                str = str.replace(",", ".");
            }
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public double parseDouble(String str) {
        if (str.contains(",")) {
            str = str.replace(",", ".");
        }
        return Double.parseDouble(str);
    }

    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasCallbackQuery()){

            String callbackData = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();

            usersStates.put(chatId, UserState.MAIN_MENU);
            usersContext.remove(chatId);
            usersMacros.remove(chatId);

            switch (callbackData) {
                case "ADD_MEAL":
                    sendAddMealMenu(chatId, messageId);
                    break;
                case "GET_RESULT":
                    sendGetResultMenu(chatId, messageId);
                    break;
                case "BACK":
                    sendMainMenu(chatId);
                    break;
                case "LEFT_TO_EAT":
                    break;
                case "ENTER_MACROS":
                    SendMessage sendMessage = SendMessage.builder()
                            .chatId(chatId)
                            .text("How much protein?")
                            .build();
                    usersStates.put(chatId, UserState.WAITING_FOR_PROTEIN);
                    usersContext.put(chatId, "meal");
                    send(sendMessage);
                    break;

            }
        }

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
                            .text("\uD83D\uDC4B Hi there!\n" +
                                    "\n" +
                                    "\uD83E\uDD57 This bot will help you track your nutrition — because we are what we eat, right?\n" +
                                    "\n" +
                                    "This is my first independent project, which I'm building to learn coding.\n" +
                                    "\n" +
                                    "The bot can (or will soon be able to):\n" +
                                    "\n" +
                                    "\uD83C\uDFAF Track your nutrition goals — protein, fat, carbs, and fiber (never skip fiber, it matters!)\n" +
                                    "\uD83C\uDF7D Log and remember every meal\n" +
                                    "\uD83D\uDCCA Track your results over time\n" +
                                    "\uD83D\uDCA1 Suggest how much more you can eat today\n" +
                                    "⭐ Save your favorite dishes so you don't have to re-enter macros every time\n" +
                                    "\uD83E\uDD16 Give personalized recommendations using AI\n" +
                                    "\n" +
                                    "\uD83D\uDCEC If you run into any issues or have suggestions — there's a feedback form, I'd love to hear from you!\n" +
                                    "☕ If you enjoy what I'm building and want to support me — there's a way to do that too.\n" +
                                    "\n" +
                                    "Let's start your journey to a healthier you.")
                            .build();
                    send(sendMessage);

                    sendMessage = SendMessage.builder()
                            .chatId(chatId)
                            .text("What's your name?")
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
                        usersMacros.get(chatId)[0] = parseDouble(message);
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
                            usersMacros.get(chatId)[1] = parseDouble(message);
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
                            usersMacros.get(chatId)[2] = parseDouble(message);
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

                            usersMacros.get(chatId)[3] = parseDouble(message);

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

        SendMessage remove = new SendMessage();
        remove.setChatId(chatId);
        remove.setText(".");
        remove.setReplyMarkup(new ReplyKeyboardRemove(true));
        send(remove);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton addMeal = new InlineKeyboardButton();
        addMeal.setText("🍽 Add meal");
        addMeal.setCallbackData("ADD_MEAL");
        row1.add(addMeal);

        InlineKeyboardButton getResult = new InlineKeyboardButton();
        getResult.setText("Get Result");
        getResult.setCallbackData("GET_RESULT");
        row1.add(getResult);

        keyboard.add(row1);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton leftToEat = new InlineKeyboardButton();
        leftToEat.setText("🍽 Left to eat");
        leftToEat.setCallbackData("LEFT_TO_EAT");
        row2.add(leftToEat);

        keyboard.add(row2);

        markup.setKeyboard(keyboard);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Choose an option:");
        message.setReplyMarkup(markup);
        send(message);
    }

    public void sendAddMealMenu(Long chatId, Integer messageId){

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();

        InlineKeyboardButton enterMacros = new InlineKeyboardButton();
        enterMacros.setText("Enter macros");
        enterMacros.setCallbackData("ENTER_MACROS");
        row1.add(enterMacros);

        InlineKeyboardButton chooseFromList = new InlineKeyboardButton();
        chooseFromList.setText("Choose from list");
        chooseFromList.setCallbackData("CHOOSE_FROM_LIST");
        row1.add(chooseFromList);

        keyboard.add(row1);

        List<InlineKeyboardButton> row2 = new ArrayList<>();

        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText("Back");
        back.setCallbackData("BACK");
        row2.add(back);

        keyboard.add(row2);

        markup.setKeyboard(keyboard);

        EditMessageText message = new EditMessageText();
        message.setChatId(chatId);
        message.setMessageId(messageId);
        message.setText("Choose an option:");
        message.setReplyMarkup(markup);
        edit(message);
    }

    public void sendGetResultMenu(Long chatId, Integer messageId){

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();

        InlineKeyboardButton today = new InlineKeyboardButton();
        today.setText("Today");
        today.setCallbackData("TODAY");
        row1.add(today);

        InlineKeyboardButton thisWeek = new InlineKeyboardButton();
        thisWeek.setText("This week");
        thisWeek.setCallbackData("THIS_WEEK");
        row1.add(thisWeek);

        keyboard.add(row1);

        List<InlineKeyboardButton> row2 = new ArrayList<>();

        InlineKeyboardButton thisMonth = new InlineKeyboardButton();
        thisMonth.setText("This month");
        thisMonth.setCallbackData("THIS_MONTH");
        row2.add(thisMonth);

        InlineKeyboardButton customPeriod = new InlineKeyboardButton();
        customPeriod.setText("Custom period");
        customPeriod.setCallbackData("CUSTOM_PERIOD");
        row2.add(customPeriod);

        keyboard.add(row2);

        List<InlineKeyboardButton> row3 = new ArrayList<>();

        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText("Back");
        back.setCallbackData("BACK");
        row3.add(back);

        keyboard.add(row3);

        markup.setKeyboard(keyboard);

        EditMessageText message = new EditMessageText();
        message.setChatId(chatId);
        message.setMessageId(messageId);
        message.setText("Choose an option:");
        message.setReplyMarkup(markup);
        edit(message);
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }
}
