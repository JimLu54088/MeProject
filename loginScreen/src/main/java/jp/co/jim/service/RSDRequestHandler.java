package jp.co.jim.service;

import jakarta.annotation.PostConstruct;
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

    @PostConstruct
    public void init() {
        handlerMap.put("JIM5P-JIM3REST", this::handleJIM5P_JIM3REST);
        handlerMap.put("JIM5P-SecurityAccess", this::handleJIM5P_SecurityAccess);
        handlerMap.put("JIMATTN-JIM3REST", this::handleJIMATTN_JIM3REST);
        handlerMap.put("JIMATTN-SecurityAccess", this::handleJIMATTN_SecurityAccess);
    }

    @Autowired
    private HandlerJIM5PRequest handlerJIM5PRequest;


    private static final Logger logger = LogManager.getLogger(RSDRequestHandler.class);
    private static final String LOG_HEADER = "[" + RSDRequestHandler.class.getSimpleName() + "] :: ";
    private static final String ERROR_LOG_HEADER = "[" + RSDRequestHandler.class.getName() + "] :: ";


    private String responseJSon = "";

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


    private void handleJIM5P_JIM3REST(String requestBody) {



        try {
            responseJSon = handlerJIM5PRequest.handler(requestBody);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleJIM5P_SecurityAccess(String requestBody) {
        // 处理 JIM5P-SecurityAccess 的逻辑
        System.out.println("went into case 2 :");
    }

    private void handleJIMATTN_JIM3REST(String requestBody) {
        // 处理 JIMATTN-JIM3REST 的逻辑
        System.out.println("went into case 3 :");
    }

    private void handleJIMATTN_SecurityAccess(String requestBody) {
        // 处理 JIMATTN-SecurityAccess 的逻辑
        System.out.println("went into case 4 :");
    }


}
