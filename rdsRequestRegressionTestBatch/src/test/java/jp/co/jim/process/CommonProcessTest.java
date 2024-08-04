package jp.co.jim.process;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

//@SpringBootTest
//@RequiredArgsConstructor
class CommonProcessTest {


    @Spy
    @InjectMocks
    public CommonProcess commonProcess;

    @Spy
    @InjectMocks
    public TestProcess testProcess;

    private AutoCloseable mockClose;

//    private WebDriver mockWebDriver;

    @BeforeEach
    public void setUp() throws IOException {

        mockClose = MockitoAnnotations.openMocks(this);
//        mockWebDriver = Mockito.mock(WebDriver.class);
        ReflectionTestUtils.setField(testProcess, "txt_REQUESTRESPONSE", "REQUESTRESPONSE");
        ReflectionTestUtils.setField(testProcess, "txt_RESPONSE", "RESPONSE");
        ReflectionTestUtils.setField(testProcess, "txt_CONTENTS", "CONTENTS");
        ReflectionTestUtils.setField(testProcess, "txt_HEADER", "HEADER");
        ReflectionTestUtils.setField(testProcess, "txt_ERRORS", "ERRORS");
        ReflectionTestUtils.setField(testProcess, "txt_ERROR", "ERROR");
        ReflectionTestUtils.setField(testProcess, "txt_REASON_CODE", "REASON_CODE");
//        ReflectionTestUtils.setField(testProcess, "driver",mockWebDriver);

        doNothing().when(testProcess).testExecution();
//        doNothing().when(testProcess).accessApp(anyString());


    }

    @AfterEach
    public void tearDown() throws Exception {
        if (testProcess.driver != null) {
            testProcess.driver.quit();
        }

        if ( commonProcess.driver != null) {
            commonProcess.driver.quit();
        }

        mockClose.close();

    }


    @Test
    public void successDeleteLocalNoFile() throws Exception {


        ReflectionTestUtils.setField(testProcess, "isJsonInOutput", true);


        String actualOp = "\"{\\\"REQUESTRESPONSE\\\":{\\\"REQUEST\\\":{\\\"HEADER\\\":{\\\"REQ_TO\\\":\\\"JIMSYSTEM\\\",\\\"REQ_ID\\\":\\\"JIM_TEST\\\",\\\"REQ_SYS_SPECIFIC\\\":{\\\"CAM_SR_ID\\\":\\\"JIM_TEST\\\",\\\"REQUEST_TYPE\\\":\\\"JIM3REST\\\"},\\\"ERRORS\\\":{},\\\"REQ_FROM\\\":\\\"JIM5P\\\"},\\\"CONTENTS\\\":{\\\"KURUMA\\\":\\\"TTTMM0001\\\",\\\"API_TYPE\\\":\\\"getCConfiGurationData\\\",\\\"IDTRANS\\\":\\\"JIM5P20145894\\\",\\\"REQ_JSON\\\":\\\"{\\\\\\\"transactionID\\\\\\\":\\\\\\\"JIM5P20145894\\\\\\\",\\\\\\\"FINFamily\\\\\\\":\\\\\\\"TTT\\\\\\\",\\\\\\\"client_req_time\\\\\\\":\\\\\\\"2024-08-03 07:51:16.454\\\\\\\",\\\\\\\"CEUDATA\\\\\\\":[{\\\\\\\"CEUProptType\\\\\\\":\\\\\\\"UUUSD\\\\\\\",\\\\\\\"CEU_ADDRESS\\\\\\\":\\\\\\\"2324D\\\\\\\",\\\\\\\"CEU_configurationLink\\\\\\\":\\\\\\\"478544TY7B\\\\\\\"}]}\\\"}},\\\"RESPONSE\\\":\\\"{\\\\\\\"CONTENTS\\\\\\\":{\\\\\\\"FILED1\\\\\\\":\\\\\\\"4564564AAA\\\\\\\",\\\\\\\"FIELD2\\\\\\\":\\\\\\\"M000\\\\\\\",\\\\\\\"FIELD3\\\\\\\":\\\\\\\"The getCConfiGurationData process is successfully.\\\\\\\"},\\\\\\\"HEADER\\\\\\\":{\\\\\\\"ERRORS\\\\\\\":{\\\\\\\"ERROR\\\\\\\":[{\\\\\\\"RETURN_CODE\\\\\\\":\\\\\\\"00\\\\\\\",\\\\\\\"REASON_CODE\\\\\\\":\\\\\\\"DDDFFF000\\\\\\\"}]}}}\\\"}}\"";

        testProcess.processOutput(actualOp, "", 4, false, "", "uuu");


    }

    @Test
    public void testAccessAppSuccess() {

    	String testURL = "http://localhost:8081/DGlogin.html";
    	testProcess.app_URL = testURL;

    	testProcess.accessApp(testURL);


    }

//    @Test
//    public void logInSuccess() {
//
//    	String testURL = "http://localhost:8081/DGlogin.html";
//    	testProcess.app_URL = testURL;
//
//    	testProcess.login("111", "1234");
//
//
//    }


}
