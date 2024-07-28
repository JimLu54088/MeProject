package jp.co.jim.service;

import com.google.gson.Gson;
import jp.co.jim.bean.utils.AppUtils;
import jp.co.jim.controller.AdminAddUser;
import jp.co.jim.entity.T_basic_kuruma_info_entity;
import jp.co.jim.entity.responseEntity.Jim5PResponseEntity;
import jp.co.jim.repository.AdminAddUserRepository;
import jp.co.jim.repository.HandlerJIM5PRequestRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class HandlerJIM5PRequest {

    private static final Logger logger = LogManager.getLogger(HandlerJIM5PRequest.class);
    private static final String LOG_HEADER = "[" + HandlerJIM5PRequest.class.getSimpleName() + "] :: ";

    private static final String ERROR_LOG_HEADER = "[" + HandlerJIM5PRequest.class.getName() + "] :: ";

    @Autowired
    private HandlerJIM5PRequestRepository handlerJIM5PRequestRepository;


    public String handler(String requestBody) throws Exception {

        String fin_number = AppUtils.getFINfromRequestJSON(requestBody);

        String apiType = AppUtils.getAPIType(requestBody);

        T_basic_kuruma_info_entity entity = getAllByFin(fin_number);

        // 创建响应实体并填充数据
        Jim5PResponseEntity responseEntity = new Jim5PResponseEntity();
        Jim5PResponseEntity.ResponseDetails responseDetails = new Jim5PResponseEntity.ResponseDetails();
        Jim5PResponseEntity.Contents contents = new Jim5PResponseEntity.Contents();
        Jim5PResponseEntity.Header header = new Jim5PResponseEntity.Header();
        Jim5PResponseEntity.Errors errors = new Jim5PResponseEntity.Errors();
        Jim5PResponseEntity.Error error = new Jim5PResponseEntity.Error();

        if (entity == null) {

            logger.info(LOG_HEADER + "The FIN:" + fin_number + " is not in DataBase.");

            contents.setField1("");
            contents.setField2("M777"); // 如果这个字段有固定值，可以在这里设置
            contents.setField3("The FIN:" + fin_number + " is not in DataBase.");

            error.setReturnCode("55");
            error.setReasonCode("ABCE008");
            errors.setError(new Jim5PResponseEntity.Error[]{error});
            header.setErrors(errors);

            responseDetails.setContents(contents);
            responseDetails.setHeader(header);
            responseEntity.setResponseDetails(responseDetails);

            // 使用Gson将响应实体转换为JSON字符串
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(responseEntity);
            logger.info(LOG_HEADER + "Response of Jim5PResponseEntity : " + jsonResponse);


            return jsonResponse;


        } else {


            contents.setField1(entity.getCDU_CMNMN_VAL());
            contents.setField2("M000"); // 如果这个字段有固定值，可以在这里设置
            contents.setField3("The " + apiType + " process is successfully.");

            error.setReturnCode("00");
            error.setReasonCode("DDDFFF000");
            errors.setError(new Jim5PResponseEntity.Error[]{error});
            header.setErrors(errors);

            responseDetails.setContents(contents);
            responseDetails.setHeader(header);
            responseEntity.setResponseDetails(responseDetails);

            // 使用Gson将响应实体转换为JSON字符串
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(responseEntity);
            logger.debug(LOG_HEADER + "Response of Jim5PResponseEntity : " + jsonResponse);

            String jsonLoginData = gson.toJson(entity);
            logger.debug(LOG_HEADER + "Response of T_basic_kuruma_info_entity : " + jsonLoginData);


            return jsonResponse;

        }


    }

    public T_basic_kuruma_info_entity getAllByFin(String fin_number) {
        return handlerJIM5PRequestRepository.getAllByFin(fin_number);
    }

}
