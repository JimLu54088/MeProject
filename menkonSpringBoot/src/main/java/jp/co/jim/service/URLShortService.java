package jp.co.jim.service;

import jp.co.jim.mainTest.URLShortener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
@Service
public class URLShortService {

    private static final String CHARACTERS = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ023456789";
    private static final int SHORT_URL_LENGTH = 6;
    private Map<String, String> urlMap = new HashMap<>();
    private Random random = new Random();

    public String shortenURL(String originalURL) {
        String shortUrl;
        do {
            shortUrl = generateShortURL();
        } while (urlMap.containsKey(shortUrl));

        urlMap.put(shortUrl, originalURL);
        return shortUrl;
    }

    public String getOriginalURL(String shortUrl) {
        return urlMap.get(shortUrl);
    }

    private String generateShortURL() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SHORT_URL_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }


}
