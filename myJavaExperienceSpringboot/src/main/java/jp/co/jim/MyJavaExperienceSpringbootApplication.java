package jp.co.jim;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class MyJavaExperienceSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyJavaExperienceSpringbootApplication.class, args);
    }

    @Bean
    public CommandLineRunner start(MyTelegramBot myTelegramBot) {
        return args -> {

            try {
                // 初始化 Bot
                TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
                botsApi.registerBot(myTelegramBot); // 註冊你的 Bot
                System.out.println("Bot is running...");
            } catch (Exception e) {
                e.printStackTrace(); // 處理可能的異常
            }

        };
    }

}
