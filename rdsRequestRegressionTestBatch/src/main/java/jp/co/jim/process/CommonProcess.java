package jp.co.jim.process;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import jakarta.annotation.PostConstruct;
import jp.co.jim.common.Constants;
import jp.co.jim.model.RequestResponseModel;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataUnit;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component("jp.co.jim.process.CommonProcess")
public class CommonProcess {

    protected WebDriver driver = null;

    protected String post = null;

    protected String app_URL = "";

    protected String test_page_URL = "";

    public String test_cases_sheet_name = "";

    public String evidence_folder_path = "";

    public String output_file_path = "";

    public String test_cases_file_path = "";

    public String user = "";

    public Map<Integer, Map<String, String>> testCases = new HashMap<>();
    public Map<String, Integer> columnNos = new HashMap<>();

    public List<Integer> testCaseNos = new ArrayList<>();

    boolean isJsonInOutput = false;

    @Setter
    String innerPath = null;

    @Setter
    String innerExpectedValue = null;

    List<Integer> executedTcNos = new ArrayList<>();
    List<String> result = new ArrayList<>();
    List<String> actualResult = new ArrayList<>();
    List<String> evidences = new ArrayList<>();

    String evidencefileName = null;

    String responseTime = null;

    String requestTime = null;


    @Value("${rootDir}")
    protected String rootDir;

    @Value("${DG_User_ID}")
    protected String DG_User_ID;

    @Value("${DG_USER_PASSWORD}")
    protected String DG_USER_PASSWORD;

    @Value("${SampleExcelFileName}")
    protected String SampleExcelFileName;


    @Value("${pathSuffix}")
    protected String pathSuffix;

    @Value("${environment}")
    protected String environment;

    @Value("${executeCounter}")
    protected String executeCounter;
    @Value("${beforeAfter}")
    protected String beforeAfter;

    @Value("${excelNameMiddle}")
    protected String excelNameMiddle;

    @Value("${isProd}")
    protected boolean isProd;

    @Value("${isVersion2}")
    protected boolean isVersion2;

    @Value("${outputFileName}")
    protected String outputFileName;

    @Value("${test_person_name}")
    protected String test_person_name;

    @Value("${test_stub_name}")
    protected String test_stub_name;

    @Value("${appURLRDSV02_DEV}")
    protected String appURLRDSV02_DEV;

    @Value("${appURLRDSV02_PROD}")
    protected String appURLRDSV02_PROD;

    @Value("${appURLNormal_DEV}")
    protected String appURLNormal_DEV;

    @Value("${appURLNormal_PROD}")
    protected String appURLNormal_PROD;

    @Value("${testPageURLDEV}")
    protected String testPageURLDEV;

    @Value("${testPageURLPROD}")
    protected String testPageURLPROD;

    @Value("${button_name_SRDV02}")
    protected String button_name_SRDV02;

    @Value("${button_name_Normal}")
    protected String button_name_Normal;

    @Value("${headers_row_no}")
    protected int headers_row_no;

    @Value("${chromeDriverPath}")
    protected String chromeDriverPath;

    @Value("${threadSleepingWaiting}")
    protected String threadSleepingWaiting;

    @Value("${waitingForAlertWindowInSecond}")
    protected int waitingForAlertWindowInSecond;

    @Value("${COL_TC_NO}")
    protected String COL_TC_NO;

    @Value("${COL_SCENARIO}")
    protected String COL_SCENARIO;

    @Value("${COL_EXECUTION}")
    protected String COL_EXECUTION;

    @Value("${COL_INPUT_JSON}")
    protected String COL_INPUT_JSON;

    @Value("${COL_API_NAME}")
    protected String COL_API_NAME;

    @Value("${COL_EXPECTED_OUTPUT}")
    protected String COL_EXPECTED_OUTPUT;

    @Value("${COL_ACTUAL_OUTPUT}")
    protected String COL_ACTUAL_OUTPUT;

    @Value("${COL_RESULT}")
    protected String COL_RESULT;

    @Value("${COL_EVIDENCE}")
    protected String COL_EVIDENCE;

    @Value("${COL_USER}")
    protected String COL_USER;

    @Value("${COL_IS_JSON_STRING_IN_OUTPUT}")
    protected String COL_IS_JSON_STRING_IN_OUTPUT;

    @Value("${COL_IS_HTTP_REQUEST}")
    protected String COL_IS_HTTP_REQUEST;

    @Value("${COL_REQUEST_URL}")
    protected String COL_REQUEST_URL;

    @Value("${COL_AUTHORIZATION}")
    protected String COL_AUTHORIZATION;

    @Value("${COL_INNER_RETN_CODE_PATH}")
    protected String COL_INNER_RETN_CODE_PATH;

    @Value("${COL_EXPECTED_CONTENTS}")
    protected String COL_EXPECTED_CONTENTS;

    @Value("${COL_CALL_METHOD}")
    protected String COL_CALL_METHOD;

    @Value("${COL_IS_GEEP}")
    protected String COL_IS_GEEP;

    @Value("${COL_IS_GET}")
    protected String COL_IS_GET;

    @Value("${is_checkAssert}")
    protected String is_checkAssert;

    @Value("${txt_REQUESTRESPONSE}")
    public String txt_REQUESTRESPONSE;

    @Value("${txt_RESPONSE}")
    public String txt_RESPONSE;

    @Value("${txt_CONTENTS}")
    public String txt_CONTENTS;

    @Value("${txt_HEADER}")
    public String txt_HEADER;

    @Value("${txt_ERRORS}")
    public String txt_ERRORS;

    @Value("${txt_ERROR}")
    public String txt_ERROR;

    @Value("${txt_RETURN_CODE}")
    public String txt_RETURN_CODE;

    @Value("${txt_REASON_CODE}")
    public String txt_REASON_CODE;

    @Value("${ROW_TEST_NAME}")
    public String ROW_TEST_NAME;

    @Value("${ROW_EXECUTOR}")
    public String ROW_EXECUTOR;

    @Value("${COL_TEST_NAME}")
    public String COL_TEST_NAME;

    @Value("${COL_EXECUTOR}")
    public String COL_EXECUTOR;


    public CommonProcess() {
        //No action
    }

    @PostConstruct
    public void init() {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
    }


    public void createDirIfNotExist(String dirPath) {
        File directory = new File(dirPath);
        if (!directory.exists()) {
            Boolean result = directory.mkdirs();
            System.out.println("----------" + result);
        }
    }

    public void accessApp(String appUrl) {
        System.out.println("go to accessApp method");

        accessApp(getDriver());

        getCurrentTime();


    }

    public void accessApp(WebDriver wd) {

        wd.get(app_URL);
        wd.manage().window().maximize();


    }

    public WebDriver getDriver() {
        if (null == driver) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("disable-popup-blocking");
            options.addArguments("ignore-certificate-error");
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(CapabilityType.UNHANDLED_PROMPT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
            options.merge(capabilities);
            try {
                driver = new ChromeDriver(options);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
        }
        ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='100%';");
        return driver;
    }


    public void getCurrentTime() {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        post = Constants.dateFormatYYYYMMDDHHMMSS.format(timestamp);


    }

    public void login(String user, String pass) {
        login(user, pass, getDriver());
    }

    public void login(String user, String pass, WebDriver driver) {


        // 定位並填充用户名和密码字段
        WebElement loginIdField = driver.findElement(By.name("username"));
        waitForms(threadSleepingWaiting);

        loginIdField.sendKeys("111");

        WebElement passwordField = driver.findElement(By.name("password"));
        waitForms(threadSleepingWaiting);

        passwordField.sendKeys("1234");

        // 单击“登录”按钮
        WebElement loginButton = driver.findElement(By.id("submit"));
        waitForms(threadSleepingWaiting);
        loginButton.click();
        waitForms(threadSleepingWaiting);

        // 等待弹出窗口出现
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitingForAlertWindowInSecond));
        wait.until(ExpectedConditions.alertIsPresent());

        // 切换到警告并接受（即点击“OK”按钮）
        Alert alert = driver.switchTo().alert();
        alert.accept();

        waitForms(threadSleepingWaiting);
    }

    public boolean readTestCases() {

        headers_row_no--;
        try (FileInputStream fis = new FileInputStream(test_cases_file_path);
             Workbook workbook = new XSSFWorkbook(fis);
        ) {

            ZipSecureFile.setMinInflateRatio(-1.0d);


            Sheet sheet = workbook.getSheet(test_cases_sheet_name);
            if (null != sheet) {
                List<String> headers = new ArrayList<>();
                List<String> tempList = new ArrayList<>();

                int rowCounter = 0;
                for (int i = headers_row_no; i < sheet.getLastRowNum() + 1; i++) {
                    Row row = sheet.getRow(i);
                    if (null != row) {
                        int lastCol = row.getLastCellNum();
                        for (int j = 0; j <= lastCol; j++) {
                            Cell cell = row.getCell(j);
                            if (null != cell) {
                                String value = null;
                                switch (cell.getCellType()) {
                                    case STRING:
                                        value = cell.getRichStringCellValue().getString();
                                        break;

                                    case NUMERIC:
                                        if (DateUtil.isCellDateFormatted(cell)) {
                                            value = "" + cell.getDateCellValue();
                                        } else {
                                            value = "" + cell.getNumericCellValue();
                                        }
                                        break;

                                    case BOOLEAN:
                                        value = "" + cell.getBooleanCellValue();
                                        break;
                                    case FORMULA:
                                        value = cell.getStringCellValue();
                                        break;
                                    default:
                                        value = "";
                                }

                                if (i == headers_row_no) {
                                    headers.add(value);
                                    columnNos.put(value, j);
                                } else if (i > 0) {
                                    try {
                                        String listVal = tempList.get(j);
                                        if ((null == listVal || "".equals(listVal))) {
                                            tempList.add(j, value);
                                        }
                                    } catch (Exception ne) {
                                        tempList.add(value);
                                    }
                                }
                            } else {
                                tempList.add(null);
                            }
                        }
                        rowCounter++;

                        if (rowCounter == 1) {
                            Integer tcNo1 = 0;
                            rowCounter = 0;
                            Map<String, String> tcData = new HashMap<>();

                            for (int ii = 0; ii < headers.size(); ii++) {
                                if (tempList.size() > ii) {
                                    if (ii == 0 && ("".equals(tempList.get(ii)) || null == tempList.get(ii))) {
                                        break;
                                    }
                                    if (COL_TC_NO.equals(headers.get(ii))) {
                                        String taiwanIsNotChina = tempList.get(ii);
                                        tcNo1 = Integer.parseInt(taiwanIsNotChina.replace(".0", ""));
                                        tcData.put(headers.get(ii), tcNo1 + "");
                                        testCaseNos.add(tcNo1);
                                    } else {
                                        tcData.put(headers.get(ii), tempList.get(ii));
                                    }
                                }
                            }
                            //-------------------------------------
                            tcData.put("ROW_NUMBER", i + "");
                            testCases.put(tcNo1, tcData);
                            tempList = new ArrayList<>();
                        }

                    } else {
                        break;
                    }
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


        return true;
    }

    public void isJsonInOutput(boolean isJsonInOutput) {
        this.isJsonInOutput = isJsonInOutput;
    }

    public void executeTestProcessLoop(String input, String expectedOutPut, String actualOP, int tcNo, boolean checkAssert, String scenario) {
        executedTcNos.add(tcNo);
        try {
//            setInput(input);
//            execute();


            setInputAndExecute(input);

            if (checkAssert) {
                actualOP = getOutput();
                processOutput(actualOP, expectedOutPut, tcNo, false, input, scenario);

                System.out.println("actualOP: " + actualOP);
            }
        } catch (Exception e) {
            result.add("Error");
            actualResult.add(actualOP);
            evidences.add("");


        }
    }

//    public void setInput(String input) {
//        setInput(input, getDriver());
//    }

    public void processOutput(String actualOP, String expectedOutput, int tcNo, Boolean isHttp, String input, String scenario) {

        responseTime = Constants.dateFormatYYYYMMDDHHMMSS.format(new Date());

        String finalReasonCode = "";

        if (null != actualOP && actualOP.startsWith("\"") && !isJsonInOutput) {
            actualOP = actualOP.replaceAll("\\\\", "");
            actualOP = actualOP.substring(1, actualOP.length() - 1);
        } else if (null != actualOP && actualOP.startsWith("\"") && isJsonInOutput) {
            actualOP = actualOP.replaceAll("\\\\\"", "\\\"");
            actualOP = actualOP.replaceAll("\\\\\"", "\\\"");
            actualOP = actualOP.substring(1, actualOP.length() - 1);


        } else {
            actualOP = actualOP.replaceAll("\\\\\"", "\\\"");
            actualOP = actualOP.replaceAll("\\\\\"", "\\\"");
            if (actualOP.startsWith("\"")) {
                actualOP = actualOP.substring(1, actualOP.length() - 1);
            }

        }


        String respJson = actualOP;
        Gson gson = new Gson();

        RequestResponseModel gObject = gson.fromJson(actualOP, RequestResponseModel.class);

        LinkedTreeMap<String, Object> res = null;
        ArrayList<LinkedTreeMap> error = new ArrayList<>();

        LinkedTreeMap<String, Object> reqRes = (LinkedTreeMap<String, Object>) gObject.get(txt_REQUESTRESPONSE);
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();

        // 解析 JSON 字符串到 Map
        Map<String, Object> map = gson.fromJson((String) reqRes.get(txt_RESPONSE), type);

        // 获取嵌套的 HEADER -> ERRORS -> ERROR -> 第一个元素 -> REASON_CODE
        if (map.containsKey("HEADER")) {
            Map<String, Object> header = (Map<String, Object>) map.get("HEADER");
            if (header.containsKey("ERRORS")) {
                Map<String, Object> errors = (Map<String, Object>) header.get("ERRORS");
                if (errors.containsKey("ERROR")) {
                    Object errorObj = errors.get("ERROR");
                    if (errorObj instanceof java.util.List) {
                        java.util.List<Map<String, Object>> errorList = (java.util.List<Map<String, Object>>) errorObj;
                        if (!errorList.isEmpty()) {
                            Map<String, Object> firstError = errorList.get(0);
                            if (firstError.containsKey("REASON_CODE")) {
                                finalReasonCode = (String) firstError.get("REASON_CODE");
//                                System.out.println("REASON_CODE: " + finalReasonCode);
                            }
                        }
                    }
                }
            }
        }


        boolean innerResult = true;
        if (null != innerPath && null != innerExpectedValue) {
            LinkedTreeMap<String, Object> contents = (LinkedTreeMap<String, Object>) res.get(txt_CONTENTS);
            String innverVal = contents.get(innerPath) + "";
            if (innverVal.contains(innerExpectedValue)) {
                innerResult = true;
            } else {
                innerResult = false;
            }

        }

        actualOP = finalReasonCode;
        System.out.println("REASON_CODE: " + actualOP);
        String evidenceFile = "";

        if (!isHttp) {
            evidenceFile = getScreenShot(tcNo + "");
        } else {
            evidenceFile = outputFileName;
        }
        boolean isSuccess = actualOP.equals(expectedOutput);
//        Boolean isSuccess = compareResult(actualOP, expectedOutput);
        isSuccess = isSuccess && innerResult;

        System.out.println("isSuccess : " + isSuccess);

        result.add((isSuccess) ? "OK" : "NG");
        actualResult.add(actualOP);
        evidences.add(evidenceFile);
        writeIntoFile(input, expectedOutput, actualOP, tcNo + "", isSuccess, respJson, scenario);


    }


    public String getScreenShot(String tcNo) {
        getDriver().manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        File src = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
        try {
            evidencefileName = tcNo + "_" + Constants.dateFormatYYYYMMDDHHMMSS.format(new Date()) + ".png";
            String EVIDENCE_filePath = evidence_folder_path + "\\" + evidencefileName;
            System.out.println("File: " + EVIDENCE_filePath);
            FileUtils.copyFile(src, new File(EVIDENCE_filePath));


        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
        return evidencefileName;

    }

    public boolean compareResult(String actualOutput, String expectedOutput) {
        return actualOutput.equals(expectedOutput);
    }

    public void goToRequstPage() {
        try {
            // 導航到目標頁面

            // 找到連結並點擊（使用完整的連結文字）
            WebElement link = driver.findElement(By.linkText("RSD Send Request Page"));
            link.click();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getOutput() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            Object obj = js.executeScript("return document.getElementById('responseJson').value;");
            return (null == obj) ? "" : obj.toString();
        } catch (Exception exception) {
            return getDriver().findElement(By.id("responseJson")).getText();
        }
    }

    public void setInputAndExecute(String input) {
        try {
            //Clean current page
            clearInput();


            // 找到 id 為 'requestJson' 的輸入框
            WebElement inputBox = driver.findElement(By.id("requestJson"));

            // 設置要輸入的字符串
            String inputString = input;
            inputBox.sendKeys(inputString);

            // 找到 id 為 'sendRequestBtn' 的按鈕
            WebElement sendButton = driver.findElement(By.id("sendRequestBtn"));

            // 點擊按鈕
            sendButton.click();

            requestTime = Constants.dateFormatYYYYMMDDHHMMSS.format(new Date());

            waitForResponse();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void writeIntoFile(String input, String expectedOuput, String output, String TC, boolean isOK, String respJSON, String scenario) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(output_file_path, true))) {

            bw.newLine();
            bw.append("----------------" + TC + " Start --------------\n")
                    .append("Scenario : ")
                    .append(scenario)
                    .append("\n")
                    .append("Time ; " + post)
                    .append("\n")
                    .append("User : " + user)
                    .append("\n")
                    .append("Input : " + input)
                    .append("\n")
                    .append("Request Time : " + requestTime)
                    .append("\n")
                    .append("Response Time : " + responseTime)
                    .append("\n")
                    .append("Expected Output : " + expectedOuput)
                    .append("\n")
                    .append("Actual Output : " + output)
                    .append("\n")
                    .append("Status : " + ((isOK) ? "Success" : "Failed"))
                    .append("\n")
                    .append("Evidence file name : " + evidencefileName)
                    .append("\n")
                    .append("----------------" + TC + " End --------------\n");


        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println(ioe.getMessage());
        }


    }

    public void writeResponseBackToExcel() {
        try (FileInputStream fis = new FileInputStream(test_cases_file_path);
             Workbook workbook = new XSSFWorkbook(fis);
             FileOutputStream fileOut = new FileOutputStream(test_cases_file_path)) {

            ZipSecureFile.setMinInflateRatio(-1.0d);

            int actResColNo = columnNos.get(COL_ACTUAL_OUTPUT);
            int resColNo = columnNos.get(COL_RESULT);
            int evidenceColNo = columnNos.get(COL_EVIDENCE);

            Sheet sheet = workbook.getSheet(test_cases_sheet_name);

            if (null != sheet) {
                int ii = 0;
                for (int tc : executedTcNos) {
                    Map<String, String> tcData = testCases.get(tc);
                    int rowNo = Integer.parseInt(tcData.get("ROW_NUMBER"));
                    Row row = sheet.getRow(rowNo);

                    Cell actResCol = row.getCell(actResColNo);
                    Cell resCol = row.getCell(resColNo);
                    Cell evidenceCol = row.getCell(evidenceColNo);

                    if (null == actResCol) {
                        actResCol = row.createCell(actResColNo);
                    }
                    if (null == resCol) {
                        resCol = row.createCell(resColNo);
                    }
                    if (null == evidenceCol) {
                        evidenceCol = row.createCell(evidenceColNo);
                    }

                    actResCol.setCellValue(actualResult.get(ii));
                    resCol.setCellValue(result.get(ii));
                    evidenceCol.setCellValue(evidences.get(ii));

                    ii++;


                }

                //Write Test Name and Executor
                int intRow_test_name = Integer.parseInt(ROW_TEST_NAME);
                int intRow_executor = Integer.parseInt(ROW_EXECUTOR);
                int intCol_test_name = Integer.parseInt(COL_TEST_NAME);
                int intCol_Executor = Integer.parseInt(COL_EXECUTOR);

                Row rowTestName = sheet.getRow(intRow_test_name);
                Row rowExcutor = sheet.getRow(intRow_executor);

                Cell celTestName = rowTestName.getCell(intCol_test_name);
                Cell celExecutor = rowExcutor.getCell(intCol_Executor);

                if (null == celTestName) {
                    celTestName = rowTestName.createCell(intCol_test_name);
                }
                if (null == celExecutor) {
                    celExecutor = rowExcutor.createCell(intCol_Executor);
                }

                celTestName.setCellValue(test_stub_name);
                celExecutor.setCellValue(test_person_name);


            }

            workbook.write(fileOut);
        } catch (IOException ioe) {
            System.out.println("Writing to Excel file failed.");
            ioe.printStackTrace();
        }


    }


    public void waitForResponse() {
        try {
            Date start = new Date();
            Boolean isNext = true;
            while (isNext) {
                if (null != getOutput() && getOutput().length() > 1) {
                    isNext = false;
                } else {
                    try {
                        Date end = new Date();
                        long seconds = (end.getTime() - start.getTime()) / 1000;
                        if (seconds > 120) {
                            isNext = false;
                        }
                        Thread.sleep(10);
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearInput() {
        // 单击“登录”按钮
        WebElement clearButton = getDriver().findElement(By.id("cleartextareaButton"));
        clearButton.click();
    }


    public void waitForms(String strms) {

        long ms = Long.parseLong(strms);
        try {
            Thread.sleep(ms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
