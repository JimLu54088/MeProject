package jp.co.jim.mainTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class URLShortener {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_URL_LENGTH = 6;
    private Map<String, String> urlMap = new HashMap<>();
    private Random random = new Random();

    public String shortenURL(String originalURL) {
        String shortUrl;
        do {
            shortUrl = generateShortURL();
        } while (urlMap.containsKey(shortUrl));

        urlMap.put(shortUrl, originalURL);
        return "http://short.url/" + shortUrl;
    }

    public String getOriginalURL(String shortUrl) {
        return urlMap.get(shortUrl.replace("http://short.url/", ""));
    }

    private String generateShortURL() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SHORT_URL_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        URLShortener urlShortener = new URLShortener();
        String shortUrl = urlShortener.shortenURL("/files/download/821b17c1-81a4-4af2-b9f6-0294aa936df3");
        System.out.println("Short URL: " + shortUrl);
        System.out.println("Original URL: " + urlShortener.getOriginalURL(shortUrl));
    }

}
