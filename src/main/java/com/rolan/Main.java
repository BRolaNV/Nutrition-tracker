package com.rolan;
import com.rolan.bot.MindfulNTbot;
import com.rolan.dao.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


@SpringBootApplication
public class Main implements CommandLineRunner {

    @Autowired
    private MindfulNTbot mindfulNTbot;

    @Override
    public void run(String... args) throws Exception {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(mindfulNTbot);
    }

    public static void main(String[] args) {
        Database.createTables();
        SpringApplication.run(Main.class, args);
    }
}
