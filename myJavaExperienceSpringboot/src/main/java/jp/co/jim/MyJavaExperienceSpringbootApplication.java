package jp.co.jim;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class MyJavaExperienceSpringbootApplication {


    @Value("${app_selenium_driver}")
    private String app_selenium_driver;

    @Value("${targetURL}")
    private String targetURL;

    @Value("${LoginuserName}")
    private String LoginuserName;

    @Value("${password}")
    private String password;


    public static void main(String[] args) {
        SpringApplication.run(MyJavaExperienceSpringbootApplication.class, args);
    }

    //For telegram bot
//    @Bean
//    public CommandLineRunner start(MyTelegramBot myTelegramBot) {
//        return args -> {
//
//            try {
//                // 初始化 Bot
//                TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
//                botsApi.registerBot(myTelegramBot); // 註冊你的 Bot
//                System.out.println("Bot is running...");
//            } catch (Exception e) {
//                e.printStackTrace(); // 處理可能的異常
//            }
//
//        };
//    }


    @Bean
    public CommandLineRunner start() {
        return args -> {

            // 設置 ChromeDriver 路徑
            System.setProperty("webdriver.chrome.driver", app_selenium_driver);

            // 設置選項，例如無頭模式
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");

            // 啟動 WebDriver
            WebDriver driver = new ChromeDriver(options);

            try {
                // 打開目標網址
                driver.get(targetURL);

                // 找到郵箱輸入框，並輸入電子郵件
                WebElement emailField = driver.findElement(By.id("_username"));
                emailField.sendKeys(LoginuserName);

                // 找到密碼輸入框，並輸入密碼
                WebElement passwordField = driver.findElement(By.id("_password"));
                passwordField.sendKeys(password);

                // 找到並點擊登入按鈕
                WebElement loginButton = driver.findElement(By.id("log_in"));
                loginButton.click();

                // 等待網頁加載（可選）
                Thread.sleep(2000);

                // 嘗試抓取包含歡迎訊息的元素
                WebElement welcomeMessage = driver.findElement(By.className("fs-16"));

                // 驗證歡迎訊息是否包含特定文字
                String messageText = welcomeMessage.getText();
                if (messageText.contains("ようこそ")) {
                    System.out.println("登入成功！");
                } else {
                    System.out.println("登入失敗，未找到歡迎訊息！");
                }
            } catch (Exception e) {
                e.printStackTrace(); // 處理可能的異常
            } finally {
                // 關閉瀏覽器
                driver.quit();
            }

        };
    }

}
