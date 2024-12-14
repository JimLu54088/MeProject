package jp.co.jim;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;


import jakarta.annotation.PostConstruct;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MyTelegramBot extends TelegramLongPollingBot {

    @Value("${yusoba_Photo_Path}")
    private String yusoba_Photo_Path;

    private final String telegram_apiToken;
    private final String telegram_botName;

    // 使用构造函数注入
    public MyTelegramBot(@Value("${telegram_apiToken}") String telegram_apiToken,
                         @Value("${telegram_botName}") String telegram_botName) {
        this.telegram_apiToken = telegram_apiToken;
        this.telegram_botName = telegram_botName;
    }

    @Value("${weatherAPi_Key}")
    private String weatherAPi_Key;

    @PostConstruct
    public void init() {
        if (telegram_apiToken == null || telegram_apiToken.isEmpty()) {
            throw new IllegalStateException("Bot token is missing");
        }
        if (telegram_botName == null || telegram_botName.isEmpty()) {
            throw new IllegalStateException("Bot username is missing");
        }
    }

    // Bot 的邏輯處理
    @Override
    public void onUpdateReceived(Update update) {
        // 檢查是否有消息且消息中是否有文本
        if (update.hasMessage()) {

            Message userRequestMessage = update.getMessage();
            Long chatId = update.getMessage().getChatId(); // 用戶的 Chat ID

            if (update.getMessage().hasText()) {
                String userMessage = update.getMessage().getText(); // 用戶發送的文本

                // 如果用戶發送 /hello
                if ("/hello".equalsIgnoreCase(userMessage)) {
                    String botReply = "Hello! You can input /dice to start dice game. /weather to get your weather."; // Bot
                    // 的回應文本
                    SendMessage message = new SendMessage(); // 建立回應消息對象
                    message.setChatId(chatId.toString()); // 設定回應目標的 Chat ID
                    message.setText(botReply); // 設定回應內容

                    try {
                        execute(message); // 發送消息
                    } catch (TelegramApiException e) {
                        e.printStackTrace(); // 處理可能的異常
                    }
                }
                // Dice function
                if ("/dice".equalsIgnoreCase(userMessage)) {
                    String botReply = "Hello! I'm RollDiceBot. Write a message in the format [x]d[y], where x is the number of rolls, and y is the number of sides on the die, and I will roll them for you. For example, 2d6 is a roll of two six-sided dice.";
                    SendMessage message = new SendMessage(); // 建立回應消息對象
                    message.setChatId(chatId.toString()); // 設定回應目標的 Chat ID
                    message.setText(botReply); // 設定回應內容

                    try {
                        execute(message); // 發送消息
                    } catch (TelegramApiException e) {
                        e.printStackTrace(); // 處理可能的異常
                    }
                }
                if (userMessage.matches("\\d+d\\d+")) {
                    SendMessage message = new SendMessage(); // 建立回應消息對象
                    message.setChatId(chatId.toString()); // 設定回應目標的 Chat ID
                    rollDice(message, userMessage);
                }
                // Weather function
                if ("/weather".equalsIgnoreCase(userMessage)) {
                    String botReply = "Hello! Please share your location to get the weather.";
                    SendMessage message = new SendMessage(); // 建立回應消息對象
                    message.setChatId(chatId.toString()); // 設定回應目標的 Chat ID
                    message.setText(botReply); // 設定回應內容

                    try {
                        execute(message); // 發送消息
                    } catch (TelegramApiException e) {
                        e.printStackTrace(); // 處理可能的異常
                    }
                }
                //Yusoba
                if ("/yusoba".equalsIgnoreCase(userMessage)) {

                    String url = "https://api.telegram.org/bot" + getBotToken() + "/sendPhoto";
                    File photo = new File(yusoba_Photo_Path); // 替换为你的本地图片路径
                    String caption = "This is an image";


                    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                        // 构建 HTTP POST 请求
                        HttpPost httpPost = new HttpPost(url);

                        // 使用 MultipartEntityBuilder 构建 multipart/form-data 数据
                        HttpEntity multipartEntity = MultipartEntityBuilder.create()
                                .addTextBody("chat_id", String.valueOf(chatId), ContentType.TEXT_PLAIN) // 添加 chat_id 参数
                                .addBinaryBody("photo", photo, ContentType.DEFAULT_BINARY, photo.getName()) // 上传文件
                                .build();

                        httpPost.setEntity(multipartEntity);

                        // 发送请求并获取响应
                        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                            System.out.println("Response Status: " + response.getCode());
                            System.out.println("Response: " + new String(response.getEntity().getContent().readAllBytes()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

            }

            if (userRequestMessage.hasLocation()) {
                double latitude = userRequestMessage.getLocation().getLatitude();
                double longitude = userRequestMessage.getLocation().getLongitude();


                // 根據經緯度查詢天氣
                try {
                    // Response JSON
                    String weather = getWeather(latitude, longitude);
                    // 創建 ObjectMapper
                    ObjectMapper objectMapper = new ObjectMapper();

                    // 將 JSON 字符串轉換為 JsonNode
                    JsonNode rootNode = objectMapper.readTree(weather);

//					JsonNode weatherArray = rootNode.get("weather"); // 获取 "weather" 节点
//
//					System.out.println("Weather Node: " + rootNode.get("weather"));
//
//					if (weatherArray != null && weatherArray.isArray() && weatherArray.size() > 0) {
//
//						 // 获取数组的第一个元素
//					    JsonNode firstWeather = weatherArray.get(0);
//
//					    System.out.println("firstWeather: " + firstWeather);
//
//					    if (firstWeather != null && firstWeather.has("description")) {
//					        // 提取 description 的值
//					        String description = firstWeather.get("description").asText();
//					        System.out.println("Weather Description: " + description);
//					    }
//
//
//					}

                    // 經度
                    String strLonggitute = rootNode.get("coord").get("lon").asText();
                    // 緯度
                    String strLatitude = rootNode.get("coord").get("lat").asText();
                    // Cloud
                    String cloudInfo = rootNode.get("weather").get(0).get("description").asText();
                    // Temp
                    String temp = rootNode.get("main").get("temp").asText();
                    // feel temp
                    String temp_feel = rootNode.get("main").get("feels_like").asText();
                    // pressure
                    String pressure = rootNode.get("main").get("pressure").asText();
                    // humidity
                    String humidity = rootNode.get("main").get("humidity").asText();
                    // wind gust
                    String strWind_gustKMperHr = "";
                    if (rootNode.get("wind").get("gust") != null) {
                        String wind_gust = rootNode.get("wind").get("gust").asText();
                        double doubleWind_gustMperS = Double.parseDouble(wind_gust);
                        double doubleWind_gustKMperHr = doubleWind_gustMperS * 3.6;
                        DecimalFormat df = new DecimalFormat("#.##");
                        strWind_gustKMperHr = df.format(doubleWind_gustKMperHr);
                    } else {
                        strWind_gustKMperHr = "沒有最大風速情報";
                    }

                    // Cloud quntity
                    String cloud_quantity = rootNode.get("clouds").get("all").asText();

                    String botReply = "Your weather is....\n" + "位置情報:\n" + "經度: " + strLonggitute + "\n" + "緯度: "
                            + strLatitude + "\n" + "雲: " + cloudInfo + "\n" + "氣溫: " + temp + " °C\n" + "體感溫度: "
                            + temp_feel + " °C\n" + "大氣壓力: " + pressure + " 百帕\n" + "濕度: " + humidity + " %\n"
                            + "最大風速: " + strWind_gustKMperHr + " (公里/小時)\n" + "雲量: " + cloud_quantity + "%\n" + "\n";

//							+ weather;
                    SendMessage message = new SendMessage(); // 建立回應消息對象
                    message.setChatId(chatId.toString()); // 設定回應目標的 Chat ID
                    message.setText(botReply); // 設定回應內容
                    execute(message); // 發送消息
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    }

    private String getWeather(double latitude, double longitude) throws Exception {

        // 目標 URL
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude
                + "&units=metric&appid=" + weatherAPi_Key;

        // 建立 HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // 建立 HttpRequest
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET() // 明確設置為 GET 請求
                .build();

        try {
            // 發送請求並接收回應
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 打印回應內容
            System.out.println("Response Status Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
            return response.body();
        } catch (IOException | InterruptedException e) {
            // 處理可能的異常
            e.printStackTrace();
            throw e;
        }

    }

    private void rollDice(SendMessage message, String rollRequest) {

        String[] parts = rollRequest.split("d");

        int numDice;
        int numFaces;

        try {
            numDice = Integer.parseInt(parts[0]);
            numFaces = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            try {
                String botReply = "Enter the command /roll [x]d[y], where x is the number of dice, and y is the number of sides on the dice. For example, /roll 3d6 - roll three six-sided dice";
                message.setText(botReply); // 設定回應內容
                execute(message); // 發送消息
            } catch (TelegramApiException ex) {
                ex.printStackTrace(); // 處理可能的異常
            }

            return;
        }

        if (numDice <= 0 || numFaces <= 0) {
            try {
                String botReply = "Sorry, the numbers must be > 0.";
                message.setText(botReply); // 設定回應內容
                execute(message); // 發送消息
            } catch (TelegramApiException ex) {
                ex.printStackTrace(); // 處理可能的異常
            }
            return;
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < numDice; i++) {
            int roll = (int) (Math.random() * numFaces) + 1;
            result.append(roll);
            if (i < numDice - 1) {
                result.append(", ");
            }
        }

        try {
            String botReply = result.toString();
            message.setText("🎲 你擲出的點數是" + botReply); // 設定回應內容
            execute(message); // 發送消息
        } catch (TelegramApiException ex) {
            ex.printStackTrace(); // 處理可能的異常
        }

    }

    @Override
    public String getBotUsername() {
        return telegram_botName; // 替換成你在 BotFather 創建的 Bot 的用戶名
    }

    @Override
    public String getBotToken() {
        return telegram_apiToken; // 替換成你從 BotFather 獲取的 Bot Token
    }
}