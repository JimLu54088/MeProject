package jp.co.jim.controller;

import com.google.gson.Gson;
import jp.co.jim.common.JwtTokenUtil;
import jp.co.jim.entity.SearchCriteriaEntity;
import jp.co.jim.entity.SearchResultEntity;
import jp.co.jim.service.LoginService;
import jp.co.jim.service.SearchResultService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ResultController {

    private static final Logger logger = LogManager.getLogger(ResultController.class);
    private static final String LOG_HEADER = "[" + ResultController.class.getSimpleName() + "] :: ";

    private static final String WARN_LOG_HEADER = "[" + ResultController.class.getSimpleName() + "] :: ";

    private static final String ERROR_LOG_HEADER = "[" + ResultController.class.getName() + "] :: ";

    @Autowired
    private SearchResultService service;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Autowired
    private Environment environment;

    @Value("${maximum_save_search_criteria}")
    private int maximum_save_search_criteria;

    @Value("${resultZipFileLocation}")
    private String resultZipFileLocation;

    @GetMapping("/getSearchResultList")
    public ResponseEntity<?> getSearchResultList(@RequestParam String userId) {


        logger.debug(LOG_HEADER + "[getSearchResultList] received userId : " + userId);

        try {
            List<SearchResultEntity> searchResultList = service.selectSearchResultByID(userId);

            Gson gson = new Gson();
            String strcriteriaList = gson.toJson(searchResultList);
            logger.debug(LOG_HEADER + "strsearchResultList : " + strcriteriaList);


            return ResponseEntity.ok(searchResultList);

        } catch (Exception ex) {
            logger.error(ERROR_LOG_HEADER + "Error while getting search result from DB : ", ex);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while getting search result from DB!!");
        }


    }


    @PostMapping("/deleteSavedsearchResult")
    public ResponseEntity<?> deleteSavedsearchResult(@RequestBody Map<String, String> requestDeleteBody) {


        logger.debug(LOG_HEADER + "[deleteSavedCriteria] received userId : " + requestDeleteBody.get("user_id") + " received s_r_id : " + requestDeleteBody.get("s_r_id") + " received ins_dt : " + requestDeleteBody.get("ins_dt"));

        try {
            service.deleteSavedsearchResult(requestDeleteBody.get("user_id"), requestDeleteBody.get("s_r_id"), requestDeleteBody.get("ins_dt"));

            return ResponseEntity.ok("{\"status\": \"ok\"}");

        } catch (Exception ex) {
            logger.error(ERROR_LOG_HEADER + "Error while delete Saved search result from DB : ", ex);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while delete Saved search result from DB!!");
        }


    }


}
