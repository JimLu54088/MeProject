package jp.com.jim.processor;

import jp.com.jim.BatchExecutor;
import jp.com.jim.common.CommonConstants;
import jp.com.jim.common.PropertyLoader;
import jp.com.jim.dto.BatchDTO;

import javax.security.auth.callback.Callback;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class BTC001Processor implements BatchProcessorIF {

    private org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(BTC001Processor.class);

    BatchDTO batchDTO = null;


    @Override
    public void process(BatchDTO batchDTO) {
        this.batchDTO = batchDTO;
        logger.info("[" + this.batchDTO.getTranscationId() + "] Process start.");

        try {


            // 1️⃣ 基本寫法：使用 Arrays.asList + split
            List<String> testCasesList = Arrays.asList(PropertyLoader.gerProperty("apiTestCases").split(","));

            for (String testCase : testCasesList) {
                apiTestExecutor(testCase);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void apiTestExecutor(String testCase) throws Exception {
        logger.info("[" + this.batchDTO.getTranscationId() + "] Test Case :: " + testCase + " start.");

        String apiUrl = PropertyLoader.gerProperty(testCase + CommonConstants.apiUrlString);

        logger.info("[" + this.batchDTO.getTranscationId() + "] API URL :: " + apiUrl);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        String strExecutionPoolCount = PropertyLoader.gerProperty("executionPoolCount");
        int intExecutionPoolCount = Integer.parseInt(strExecutionPoolCount);


        ExecutorService executorService = Executors.newFixedThreadPool(intExecutionPoolCount);

        long startTime = 0;
        long endTime = 0;

        String apiType = PropertyLoader.gerProperty(testCase + CommonConstants.apiTypeString);

        logger.info("[" + this.batchDTO.getTranscationId() + "] API TYPE :: " + apiType);

        AtomicReference<String> res = new AtomicReference<>("");
        startTime = System.currentTimeMillis();

        String parameter = PropertyLoader.gerProperty(testCase + CommonConstants.apiTestRequestBodyString);

        logger.info("[" + this.batchDTO.getTranscationId() + "] API Parameter :: " + parameter);

        String successResponseContent = PropertyLoader.gerProperty(testCase + CommonConstants.apiTestExpectedResponseString);

        String strExecutionCount = PropertyLoader.gerProperty("executeCount");

        int intExecutionCount = Integer.parseInt(strExecutionCount);
        int totalExecutionCount = intExecutionCount * intExecutionPoolCount;

        Callable<Void> task = () -> {

            for (int i = 0; i < intExecutionCount; i++) {

                res.set(callService(parameter, apiUrl, null, apiType));

                if (res.get().contains(successResponseContent)) {
                    successCount.incrementAndGet();
                } else {
                    failCount.incrementAndGet();
                }


            }


            return null;

        };

        Future<Void> future1 = executorService.submit(task);
        Future<Void> future2 = executorService.submit(task);
        Future<Void> future3 = executorService.submit(task);
        Future<Void> future4 = executorService.submit(task);

        future1.get();
        future2.get();
        future3.get();
        future4.get();

        executorService.shutdown();

        endTime = System.currentTimeMillis();

        long executionTime = endTime - startTime;

        logger.info("[" + this.batchDTO.getTranscationId() + "] Success Count :: " + successCount.get());
        logger.info("[" + this.batchDTO.getTranscationId() + "] Fail Count :: " + failCount.get());
        logger.info("[" + this.batchDTO.getTranscationId() + "] Execution Time :: " + executionTime + " (ms)");

        logger.info("[" + this.batchDTO.getTranscationId() + "] Test Case :: " + testCase + " end.");


    }

    private String callService(String input, String urlString, String authorization, String apiType) {
        StringBuilder response = new StringBuilder();
        HttpURLConnection conn = null;

        if (null == input) {
            input = "";
        }
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(apiType);                    // ★ 固定 GET
            conn.setConnectTimeout(10_000);                  // 連線逾時 10 秒
            conn.setReadTimeout(10_000);                     // 讀取逾時 10 秒
            conn.setRequestProperty("Accept", "application/json");

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) conn.disconnect();
        }


        return response.toString();
    }


}
