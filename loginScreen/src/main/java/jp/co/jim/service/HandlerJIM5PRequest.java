package jp.co.jim.service;

import jp.co.jim.bean.utils.AppUtils;
import org.springframework.stereotype.Service;

@Service
public class HandlerJIM5PRequest {
    public String handler(String requestBody) throws Exception {

        String fin_number = AppUtils.getFINfromRequestJSON(requestBody);


        return fin_number;
    }
}
