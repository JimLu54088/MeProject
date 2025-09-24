package jp.com.jim.common;

import java.util.Date;
import java.util.Random;

public class APPUtils {

    public static String createTrakingId(String batchId) {
        return batchId + "_" + CommonConstants.dateTimeFormat.format(new Date()) + "_" + getRandomNumber(100, 900);
    }

    public static int getRandomNumber(int start, int end) {
        Random random = new Random();
        // Generate a random 3-digit number
        return random.nextInt(end) + start;
    }


}
