package jp.co.jim.service;


import jp.co.jim.repository.Delete_T_H_Op_repository;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;

@Service
public class Delete_T_H_Op_service {

    @Value("${histTableList}")
    private String histTableList;

    @Value("${deleteRetentionInDay}")
    private int deleteRetentionInDay;

    @Value("${commitIntervalForDeleteHistTables}")
    private int commitIntervalForDeleteHistTables;


    @Autowired
    private Delete_T_H_Op_repository repository;

    private static final Logger logger = LogManager.getLogger(Delete_T_H_Op_service.class);
    private static final String LOG_HEADER = "[" + Delete_T_H_Op_service.class.getSimpleName() + "] :: ";
    private static final String ERROR_LOG_HEADER = "[" + Delete_T_H_Op_service.class.getName() + "] :: ";

    public void delete_T_hist_table() {

        List<String> histTables = new ArrayList(Arrays.asList(StringUtils.splitPreserveAllTokens(histTableList, ",")));


        logger.info(LOG_HEADER + "deleteRetentionInDay" + " :: " + deleteRetentionInDay);

        try {
            deleteHistoryData(histTables, deleteRetentionInDay);
        } catch (Exception e) {
            logger.error(ERROR_LOG_HEADER + "exception :: " + e);
        }

    }


    private void deleteHistoryData(List<String> histTables, int retentionPeriod) throws SQLException {
        for (String tableName : histTables) {

            long startTime = System.currentTimeMillis();

            long affectedRows;
            do {
                affectedRows = repository.delete_T_hist_table(tableName, commitIntervalForDeleteHistTables, getDate(retentionPeriod));

            } while (affectedRows == commitIntervalForDeleteHistTables);


            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            logger.debug(LOG_HEADER, "Delete Table : " + tableName + " Execution Time(ms) : " + executionTime);


        }
    }

    private Date getDate(int retentionPeriod) {
        Date t = null;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, retentionPeriod * -1);
        t = cal.getTime();
        return t;
    }


}
