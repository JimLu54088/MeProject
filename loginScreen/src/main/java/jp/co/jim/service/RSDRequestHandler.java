package jp.co.jim.service;

import jp.co.jim.bean.utils.AppUtils;
import jp.co.jim.controller.SendRSDRequestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class RSDRequestHandler {

    private static final Map<String, Consumer<String>> handlerMap = new HashMap<>();

    static {
        handlerMap.put("JIM5P-JIM3REST", RSDRequestHandler::handleJIM5P_JIM3REST);
        handlerMap.put("JIM5P-SecurityAccess", RSDRequestHandler::handleJIM5P_SecurityAccess);
        handlerMap.put("JIMATTN-JIM3REST", RSDRequestHandler::handleJIMATTN_JIM3REST);
        handlerMap.put("JIMATTN-SecurityAccess", RSDRequestHandler::handleJIMATTN_SecurityAccess);
    }


    private static final Logger logger = LogManager.getLogger(RSDRequestHandler.class);
    private static final String LOG_HEADER = "[" + RSDRequestHandler.class.getSimpleName() + "] :: ";
    private static final String ERROR_LOG_HEADER = "[" + RSDRequestHandler.class.getName() + "] :: ";

    private static String responseJSon = "";

    public String rsdRequestHandler(String requestBody) throws Exception {

        String req_type = AppUtils.getREQ_TYPEfromRequestJSON(requestBody);

        String req_from = AppUtils.getREQ_FROMfromRequestJSON(requestBody);

        logger.info(LOG_HEADER + "req_type: " + req_type);

        logger.info(LOG_HEADER + "req_from: " + req_from);

        String key = req_from + "-" + req_type;
        Consumer<String> handler = handlerMap.get(key);

        if (handler != null) {
            handler.accept(requestBody);
        } else {
            logger.info("No handler found for key: " + key);
        }


        return responseJSon;
    }


    private static void handleJIM5P_JIM3REST(String requestBody) {
        // 处理 JIM5P-JIM3REST 的逻辑
        System.out.println("went into case 1 :");

        HandlerJIM5PRequest handlerJIM5PRequest = new HandlerJIM5PRequest();

        try{
            responseJSon =   handlerJIM5PRequest.handler(requestBody);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private static void handleJIM5P_SecurityAccess(String requestBody) {
        // 处理 JIM5P-SecurityAccess 的逻辑
        System.out.println("went into case 2 :");
    }

    private static void handleJIMATTN_JIM3REST(String requestBody) {
        // 处理 JIMATTN-JIM3REST 的逻辑
        System.out.println("went into case 3 :");
    }

    private static void handleJIMATTN_SecurityAccess(String requestBody) {
        // 处理 JIMATTN-SecurityAccess 的逻辑
        System.out.println("went into case 4 :");
    }


}
