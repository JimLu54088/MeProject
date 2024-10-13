package jp.co.jim.service;


import jp.co.jim.common.Constants;
import jp.co.jim.repository.Delete_T_H_Op_repository;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

@Service
public class Delete_T_H_Op_service {

    @Value("${histTableList}")
    private String histTableList;

    @Value("${deleteRetentionInDay}")
    private int deleteRetentionInDay;

    @Value("${commitIntervalForDeleteHistTables}")
    private int commitIntervalForDeleteHistTables;

    @Value("${LogPath}")
    private String logPath;


    @Autowired
    private Delete_T_H_Op_repository repository;

    private static final Logger logger = LogManager.getLogger(Delete_T_H_Op_service.class);
    private static final String LOG_HEADER = "[" + Delete_T_H_Op_service.class.getSimpleName() + "] :: ";
    private static final String ERROR_LOG_HEADER = "[" + Delete_T_H_Op_service.class.getName() + "] :: ";

    public void delete_T_hist_tableAndOldLog() throws Exception {

        List<String> histTables = new ArrayList(Arrays.asList(StringUtils.splitPreserveAllTokens(histTableList, ",")));


        logger.info(LOG_HEADER + "deleteRetentionInDay" + " :: " + deleteRetentionInDay);

        if (histTables.size() == 0) {

            logger.warn(LOG_HEADER + "No target history table found in property file, no table will be delete.");

        } else {

            try {
                deleteHistoryData(histTables, deleteRetentionInDay);
            } catch (Exception e) {
                logger.error(ERROR_LOG_HEADER + "exception :: " + e);
                throw e;
            }

        }

        //Delete old Logs
        deleteOldLogs(deleteRetentionInDay, logPath);


    }


    private void deleteHistoryData(List<String> histTables, int retentionPeriod) throws SQLException {

        //Get delete date format: 2000-10-10 00:00:00
        String deleteDate = Constants.dateTimeFormatyyyyMMddHHmmss.format(getDate(retentionPeriod));

        logger.debug(LOG_HEADER + "deleteDate" + " :: " + deleteDate);


        for (String tableName : histTables) {

            long startTime = System.currentTimeMillis();

            long affectedRows;
            do {
                affectedRows = repository.delete_T_hist_table(tableName, commitIntervalForDeleteHistTables, deleteDate);

            } while (affectedRows == commitIntervalForDeleteHistTables);


            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            logger.debug(LOG_HEADER + "Delete Table : " + tableName + " Execution Time(ms) : " + executionTime);


        }
    }

    private void deleteOldLogs(int retentionPeriod, String logPath) throws IOException {


        logger.debug(LOG_HEADER + "delete old log path" + " :: " + logPath);

        LocalDateTime xxxDaysAgo = getDate(retentionPeriod).toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        AtomicBoolean fileDeleted = new AtomicBoolean(false);  // 使用 AtomicBoolean 記錄是否有檔案被刪除

        logger.debug(LOG_HEADER + retentionPeriod +" DaysAgo" + " :: " + xxxDaysAgo);

        Path pathLogpath = Paths.get(logPath);


        try (Stream<Path> files = Files.list(pathLogpath)) {
            files.forEach(file -> {
                try {
                    // 取得檔案的最後修改時間
                    BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);
                    FileTime lastModifiedTime = attrs.lastModifiedTime();
                    LocalDateTime fileModifiedTime = LocalDateTime.ofInstant(lastModifiedTime.toInstant(), ZoneId.systemDefault());

                    // 檢查檔案副檔名和修改時間是否超過 10 天
                    if (fileModifiedTime.isBefore(xxxDaysAgo) &&
                            (file.toString().endsWith(".log") || file.toString().endsWith(".gz"))) {
                        // 刪除檔案
                        Files.delete(file);
                        logger.debug(LOG_HEADER + "Deleted" + " :: " + file.toString());
                        fileDeleted.set(true);  // 使用 AtomicBoolean 設置值 標記至少刪除了一個檔案
                    }

                } catch (IOException e) {
                    logger.error(ERROR_LOG_HEADER + "Error processing file: " + file.toString());
                    throw new RuntimeException(e);
                }
            });


            if (fileDeleted.get()) {
                logger.info(LOG_HEADER + "Delete old Logs before " + xxxDaysAgo + " in " + logPath + " successfully!!");
            } else {
                logger.info(LOG_HEADER + "No log older than " + xxxDaysAgo + " was found for deletion.");
            }


        } catch (IOException e) {
            System.err.println("Error accessing folder: " + logPath);
            logger.error(ERROR_LOG_HEADER + "Error accessing folder: " + logPath);
            throw new RuntimeException(e);

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
