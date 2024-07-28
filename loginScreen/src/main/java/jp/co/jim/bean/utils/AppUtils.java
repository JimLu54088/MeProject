package jp.co.jim.bean.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import jp.co.jim.common.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutionException;

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

    public static String getREQ_TYPEfromRequestJSON(String requestBodyJSON) throws Exception {

        String requestType = "";
        try {

            JsonNode rootNode = mapper.readTree(requestBodyJSON);
            JsonNode requestTypeNode = rootNode.path("REQUESTRESPONSE")
                    .path("REQUEST")
                    .path("HEADER")
                    .path("REQ_SYS_SPECIFIC")
                    .path("REQUEST_TYPE");
            requestType = requestTypeNode.asText();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestType;
    }

    public static String getREQ_FROMfromRequestJSON(String requestBodyJSON) throws Exception {

        String requestFrom = "";
        try {

            JsonNode rootNode = mapper.readTree(requestBodyJSON);
            JsonNode requestFromNode = rootNode.path("REQUESTRESPONSE")
                    .path("REQUEST")
                    .path("HEADER")
                    .path("REQ_FROM");
            requestFrom = requestFromNode.asText();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestFrom;


    }

    public static String updateClient_req_timeInJSON(String requstBodyJSON) throws Exception {
        String updatedJson = "";
        try {

            JsonNode rootNode = mapper.readTree(requstBodyJSON);

            // 获取当前时间并格式化
            String currentTime = Constants.dateTimeFormatyyyyMMdd_HHmmssSSS.format(new Date());

            // 获取到 REQ_JSON 对象节点
            JsonNode requestJsonNode = rootNode.path("REQUESTRESPONSE")
                    .path("REQUEST")
                    .path("CONTENTS")
                    .path("REQ_JSON");

            // 解析 REQ_JSON 的 JSON 字符串
            ObjectNode reqJsonObject = (ObjectNode) mapper.readTree(requestJsonNode.asText());

            // 设置 client_req_time
            reqJsonObject.put("client_req_time", currentTime);

            // 更新 REQ_JSON 字段
            ((ObjectNode) rootNode.path("REQUESTRESPONSE")
                    .path("REQUEST")
                    .path("CONTENTS"))
                    .put("REQ_JSON", reqJsonObject.toString());

            // 打印更新后的 JSON (Pretty version)
//            updatedJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
            // 打印更新后的 JSON (Normal version)
            updatedJson = mapper.writeValueAsString(rootNode);
            System.out.println("updatedJson: " + updatedJson);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return updatedJson;
    }

    public static String getFINfromRequestJSON(String requestBodyJSON) throws Exception {

        String fin_number = "";
        try {

            JsonNode rootNode = mapper.readTree(requestBodyJSON);
            JsonNode requestFromNode = rootNode.path("REQUESTRESPONSE")
                    .path("REQUEST")
                    .path("CONTENTS")
                    .path("KURUMA");
            fin_number = requestFromNode.asText();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return fin_number;


    }
}
