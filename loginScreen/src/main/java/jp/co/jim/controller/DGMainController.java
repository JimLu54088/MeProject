package jp.co.jim.controller;

import com.google.gson.Gson;
import jp.co.jim.entity.DGMainEntity;
import jp.co.jim.service.UserActionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class DGMainController {

    @Autowired
    private UserActionService userActionService;

    private static final Logger logger = LogManager.getLogger(DGMainController.class);
    private static final String LOG_HEADER = "[" + DGMainController.class.getSimpleName() + "] :: ";
    private static final String ERROR_LOG_HEADER = "[" + DGMainController.class.getName() + "] :: ";

    @GetMapping("/getdropDownSelect")
    public ResponseEntity<?> getDropdownDGMainOptions() {

        logger.debug(LOG_HEADER + "=========================Starting retriving selecting of DGMain.=========================");

        List<DGMainEntity> list = userActionService.selectAll();

        //Output received reuqestBody
        Gson gson = new Gson();
        String jsonList = gson.toJson(list);
        logger.debug(LOG_HEADER + "Json list of DGMain dropdown options : " + jsonList);


        // Validate the credentials

        if (!list.isEmpty()) {
            return ResponseEntity.ok(list);

        } else {
            logger.error(LOG_HEADER + "Get dropdown options for DGMain failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Get dropdown options for DGMain failed.");
        }


    }
}
