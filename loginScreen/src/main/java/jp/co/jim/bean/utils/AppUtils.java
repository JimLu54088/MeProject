package jp.co.jim.bean.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import jp.co.jim.common.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

@SuppressWarnings("deprecation")
public class AppUtils {

    private static ObjectMapper mapper = null;

    private static final Logger logger = (Logger) LogManager.getLogger(AppUtils.class);

    static {
        if (mapper == null) {
            mapper = new ObjectMapper();
        }

        mapper.disable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new ISO8601DateFormat());
    }


    public static String createTrackingId(String batchId) {
        return batchId + "_" + Constants.dateTimeFormatyyyyMMdd_HHmmss.format(new Date()) + "_" + getRandomNumber(100, 900);
    }

    public static int getRandomNumber(int start, int end) {
        Random random = new Random();

        return random.nextInt(end) + start;
    }

    public static Object getObjectFromJson(String json) throws JsonProcessingException {
        return mapper.readValue(json, Object.class);
    }

    public static boolean checkRequstBodyJsonValidation(String requestBodyJSON) {
        try {
            mapper.readTree(requestBodyJSON);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
