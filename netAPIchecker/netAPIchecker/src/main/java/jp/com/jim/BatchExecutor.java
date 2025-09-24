package jp.com.jim;

import jp.com.jim.common.PropertyLoader;
import jp.com.jim.dto.BatchDTO;
import jp.com.jim.processor.BatchProcessorIF;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import jp.com.jim.common.APPUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class BatchExecutor {

    private static org.apache.logging.log4j.Logger logger = null;

    public static void main(String[] args) {

        try {
            if (args.length == 0 || args[0] == null || args[0].isEmpty()) {
                System.err.println(
                        "ERROR: Property File Name is not given. Please provide info -Dproperty.file=<Path of property file>");
                System.exit(-1);
            }

            String sPropertyFile = args[0];
            PropertyLoader.propertyFilePath = sPropertyFile;

            String log4jPath = PropertyLoader.gerProperty("log4jPath");

            initializeLogging(log4jPath);

            logger.info("----------Process started ------------");

            String batchID = PropertyLoader.gerProperty("BatchId");
            String transcationID = APPUtils.createTrakingId(batchID);

            BatchDTO batchDTO = new BatchDTO();
            batchDTO.setBatchId(batchID);
            batchDTO.setTranscationId(transcationID);

            String executionTimeIntervalFlag = PropertyLoader.gerProperty("executionTimeIntervalFlag");

            DayOfWeek executionTimeCheckStartTimeDay = DayOfWeek.valueOf(PropertyLoader.gerProperty("executionTimeCheckStartTimeDay"));
            LocalTime executionTimeCheckStartTimeHourMinute = LocalTime.parse(PropertyLoader.gerProperty("executionTimeCheckStartTimeHourMinute"));

            DayOfWeek executionTimeCheckEndTimeDay = DayOfWeek.valueOf(PropertyLoader.gerProperty("executionTimeCheckEndTimeDay"));
            LocalTime executionTimeCheckEndTimeHourMinute = LocalTime.parse(PropertyLoader.gerProperty("executionTimeCheckEndTimeHourMinute"));

            logger.info("[" + batchDTO.getTranscationId() + "] " + "executionTimeIntervalFlag :: " + executionTimeIntervalFlag);


            if (checkWithinConfiguredInterval(executionTimeIntervalFlag, executionTimeCheckStartTimeDay, executionTimeCheckStartTimeHourMinute, executionTimeCheckEndTimeDay, executionTimeCheckEndTimeHourMinute)) {
                logger.info("[" + batchDTO.getTranscationId() + "] " + "The current time is in " + executionTimeCheckStartTimeDay + " " + executionTimeCheckStartTimeHourMinute + " ~ " + executionTimeCheckEndTimeDay + " " + executionTimeCheckEndTimeHourMinute + ", so start API checking batch process.");

                String processorClassName = PropertyLoader.gerProperty("processorClass");

                Class<?> clazz = Class.forName(processorClassName);

                Object instance = clazz.getDeclaredConstructor().newInstance();

                if (instance instanceof BatchProcessorIF processor) {
                    processor.process(batchDTO);
                } else {
                    throw new RuntimeException("指定的類不是 BatchProcessor: " + processorClassName);
                }


            } else {
                logger.info("[" + batchDTO.getTranscationId() + "] " + "The current time is not in " + executionTimeCheckStartTimeDay + " " + executionTimeCheckStartTimeHourMinute + " ~ " + executionTimeCheckEndTimeDay + " " + executionTimeCheckEndTimeHourMinute + ", so skip API checking batch process.");

            }

            logger.info("[" + batchDTO.getTranscationId() + "] " + "----------Process ended ------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initializeLogging(String log4jPath) {
        // *** 先初始化 ***
        org.apache.logging.log4j.core.config.Configurator.initialize(null, "file:///" + log4jPath);

        // *** 再建立 logger ***
        logger = org.apache.logging.log4j.LogManager.getLogger(BatchExecutor.class);

        logger.debug("Log4j2 已載入指定的設定檔！");
    }

    public static boolean isNowWithinRange(
            DayOfWeek startDay, LocalTime startTime,
            DayOfWeek endDay, LocalTime endTime) {

        LocalDateTime now = LocalDateTime.now();
        DayOfWeek today = now.getDayOfWeek();

        // 找出「本週」的開始與結束時間
        LocalDate todayDate = LocalDate.now();
        LocalDate startDate = todayDate.with(startDay);
        LocalDate endDate = todayDate.with(endDay);

        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

        return !now.isBefore(startDateTime) && !now.isAfter(endDateTime);
    }


    private static boolean checkWithinConfiguredInterval(String executionTimeIntervalFlag, DayOfWeek startDay, LocalTime startTime, DayOfWeek endDay, LocalTime endTime) throws Exception {
        if (!"true".equals(executionTimeIntervalFlag)) return true;
        return isNowWithinRange(startDay, startTime, endDay, endTime);
    }


}

