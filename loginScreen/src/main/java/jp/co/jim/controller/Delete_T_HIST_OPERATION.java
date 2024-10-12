package jp.co.jim.controller;

import com.google.gson.Gson;
import jp.co.jim.entity.UserEntity;
import jp.co.jim.service.Delete_T_H_Op_service;
import jp.co.jim.service.UserActionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class Delete_T_HIST_OPERATION {

    @Autowired
    private Delete_T_H_Op_service service;

    private static final Logger logger = LogManager.getLogger(Delete_T_HIST_OPERATION.class);
    private static final String LOG_HEADER = "[" + Delete_T_HIST_OPERATION.class.getSimpleName() + "] :: ";
    private static final String ERROR_LOG_HEADER = "[" + Delete_T_HIST_OPERATION.class.getName() + "] :: ";

    @GetMapping("/deleteHistAndLog")
    public ResponseEntity<String> deleteHistAndLog() {

        logger.info(LOG_HEADER + "Get into deleteHistAndLog");

        service.delete_T_hist_table();


        return ResponseEntity.ok("DGPasswordChange successfully.");
    }


}
