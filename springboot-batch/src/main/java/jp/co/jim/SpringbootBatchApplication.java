package jp.co.jim;

import jp.co.jim.bean.BatchDetails;
import jp.co.jim.bean.ErrorDTO;
import jp.co.jim.bean.utils.AppUtils;
import jp.co.jim.bean.utils.LogUtil;
import jp.co.jim.common.AppFactory;
import jp.co.jim.common.CommonConstants;
import jp.co.jim.db.entity.T_EXECUTION_HIST;
import jp.co.jim.db.repository.T_EXECUTION_HIST_Repository;
import jp.co.jim.db.service.T_EXECUTION_HISTService;
import jp.co.jim.processor.BatchProcessorIf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import jp.co.jim.service.BatchProcessor;

import java.util.Date;

@SpringBootApplication
@ComponentScan(basePackages = "jp.co.jim")
public class SpringbootBatchApplication {

    @Value("${applicationName}")
    private String applicationName;

    @Value("${batchId}")
    private String batchId;

    @Value("${processorClass}")
    private String processorClass;

    @Value("${startedStatusCode}")
    private int startedStatusCode;

    @Value("${warningStatusCode}")
    private int warningStatusCode;

    @Value("${successStatusCode}")
    private int successStatusCode;


    private Boolean alreadyStarted = false;

    private long runningCount = 0L;


    private static final Logger logger = (Logger) LogManager.getLogger(SpringbootBatchApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringbootBatchApplication.class, args);
    }

    @Autowired
    private BatchProcessor service;

    @Autowired
    private BatchDetails batchDetails;

    @Autowired
    private AppFactory appFactory;

    @Autowired
    private T_EXECUTION_HISTService t_EXECUTION_HISTService;


    /**
     * Start batch from here
     *
     * @return
     */

//	@Bean
//	public CommandLineRunner start() {
//		return args -> {
//			System.out.println("-----------------------");
//			service.writeComehere();
//		};
//	}
    @Bean
    public CommandLineRunner start() {
        return args -> {

            try {
                logger.info("====================================================================");
                logger.info("Application started : " + applicationName);
                logger.info("Batch Id : " + batchId);

                //Set details to batch details for processing
                batchDetails.setApplicationName(applicationName);
                batchDetails.setBatchId(batchId);
                batchDetails.setStartTime(new Date());

                //Create tracking id
                LogUtil.debug(logger, CommonConstants.MSG_CREATE_TRKING_ID, batchDetails);
                batchDetails.setTrackingId(AppUtils.createTrackingId(batchId));
                batchDetails.setExecutionStatus(startedStatusCode);

                //Validate already running or not
                alreadyStarted = checkForSameBatchRunning(batchId);

                //If batch already running log warning and skip processing
                if (alreadyStarted) {
                    batchDetails.addWarning(new ErrorDTO(CommonConstants.SMBW01, CommonConstants.MSG_SMBW01));
                    batchDetails.setExecutionStatus(warningStatusCode);
                    LogUtil.warn(logger, CommonConstants.SMBW01, CommonConstants.MSG_SMBW01, batchDetails);

                    runningCount = getAlreadyStartCount(batchId);

                    String message = "The process is already running and not finished yet. "
                            + "\n Please confirm processing. This is [" + runningCount + "] time.";
                    LogUtil.warn(logger, message, batchDetails);

                    updateBatchStatus();
                    return;
                }

                //Start batch
                logBatchProcessStart();

                //Get procession class
                BatchProcessorIf processor = getProcessorClass();

                LogUtil.info(logger, "Process started.", batchDetails);
                //Process batch
                processor.process();
                LogUtil.info(logger, "Process finished.", batchDetails);


                batchDetails.setExecutionStatus(successStatusCode);


            } catch (Exception e) {

            } catch (Throwable e) {
                throw new RuntimeException(e);
            } finally {
                //Update DB status
                postProcess();
                logger.info("====================================================================");

            }


        };
    }

    private BatchProcessorIf getProcessorClass() throws Exception {
        BatchProcessorIf processor = null;
        try {
            //If null create default processor class
            if (null == processorClass || processorClass.isEmpty()) {
                processor = appFactory.createInstance(BatchProcessor.class.getName());
            } else {
                processor = appFactory.createInstance(processorClass);
            }
        } catch (Exception e) {
            //logerror
            throw e;
        }
        return processor;
    }

    private Boolean checkForSameBatchRunning(String batchId) {
        LogUtil.debug(logger, "validateAlreadyStarted started.,", batchDetails);
        Long count = t_EXECUTION_HISTService.getRunningBatchCountByBatchID(batchDetails.getBatchId());
        if (null == count || count == 0L) {
            LogUtil.debug(logger, "ValidateAlreadyStarted ifnished.", batchDetails);
            return false;
        }
        LogUtil.debug(logger, "ValidatedAlreadyStarted finished.", batchDetails);
        return true;
    }

    private void logBatchProcessStart() {
        LogUtil.debug(logger, "logBatchProcessStart", batchDetails);
        T_EXECUTION_HIST trnHist = new T_EXECUTION_HIST();
        trnHist.setEXEC_ID(batchDetails.getTrackingId());
        trnHist.setPRCS_ID(batchDetails.getBatchId());
        trnHist.setSTART_TIME(batchDetails.getStartTime());
        trnHist.setINS_BY(batchDetails.getBatchId());
        trnHist.setSTATUS_CODE(batchDetails.getExecutionStatus());

        trnHist = t_EXECUTION_HISTService.save(trnHist);
        batchDetails.setT_EXECUTION_HIST(trnHist);

        LogUtil.debug(logger, "logBatchProcessStart finished", batchDetails);
    }

    private void updateBatchStatus() {
        LogUtil.debug(logger, "Update batch status for already started.", batchDetails);
        T_EXECUTION_HIST trnHist = new T_EXECUTION_HIST();
        trnHist.setEXEC_ID(batchDetails.getTrackingId());
        trnHist.setPRCS_ID(batchDetails.getBatchId());
        trnHist.setSTART_TIME(batchDetails.getStartTime());
        trnHist.setINS_BY(batchDetails.getBatchId());
        trnHist.setSTATUS_CODE(batchDetails.getExecutionStatus());
        trnHist.setEND_TIME(new Date());
        trnHist.setUPD_DT(new Date());
        trnHist.setUPD_BY(batchDetails.getBatchId());

        trnHist = t_EXECUTION_HISTService.save(trnHist);

        LogUtil.debug(logger, "Update batch status for already started completed.", batchDetails);

        trnHist = t_EXECUTION_HISTService.save(trnHist);
        batchDetails.setT_EXECUTION_HIST(trnHist);

        LogUtil.debug(logger, "logBatchProcessStart finished", batchDetails);
    }

    private Long getAlreadyStartCount(String batchId) {
        LogUtil.debug(logger, "getAlreadyStartCount started.", batchDetails);
        return t_EXECUTION_HISTService.getStartFailedCountByBatchId(batchDetails.getBatchId());
    }

    private void postProcess() {
        //return if already Started
        if (alreadyStarted) {
            return;
        }

        LogUtil.debug(logger, "postProcss started.", batchDetails);
        T_EXECUTION_HIST trnHist = batchDetails.getT_EXECUTION_HIST();
        if (null == trnHist) {
            return;
        }

        trnHist.setEND_TIME(new Date());
        trnHist.setUPD_DT(new Date());
        trnHist.setUPD_BY(batchDetails.getBatchId());

        //If errors in the error list status will be error.
//        if(batchDetails.getErrorCount() > 0){
//
//        }else if(warning){
//
//        }
//        else {
        trnHist.setSTATUS_CODE(batchDetails.getExecutionStatus());
//        }
        //update details
        trnHist = t_EXECUTION_HISTService.save(trnHist);

        LogUtil.debug(logger, "postProcess finished.", batchDetails);


    }


}
