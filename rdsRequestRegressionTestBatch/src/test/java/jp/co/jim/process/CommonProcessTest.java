package jp.co.jim.process;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;


class CommonProcessTest {


//    @Spy
//    @InjectMocks
//    public CommonProcess commonProcess;

//    private AutoCloseable mockClose;

//    @BeforeEach
//    public void setUp() {
//
//        mockClose = MockitoAnnotations.openMocks(this);
//
//
//    }
//
//    @AfterEach
//    public void tearDown() throws Exception {
//        mockClose.close();
//
//    }


    @Test
    public void successDeleteLocalNoFile() throws Exception {


        CommonProcess commonProcess = new CommonProcess();

        ReflectionTestUtils.setField(commonProcess, "txt_REQUESTRESPONSE", "REQUESTRESPONSE");
        ReflectionTestUtils.setField(commonProcess, "txt_RESPONSE", "RESPONSE");
        ReflectionTestUtils.setField(commonProcess, "txt_CONTENTS", "CONTENTS");
        ReflectionTestUtils.setField(commonProcess, "txt_HEADER", "HEADER");
        ReflectionTestUtils.setField(commonProcess, "txt_ERRORS", "ERRORS");
        ReflectionTestUtils.setField(commonProcess, "txt_ERROR", "ERROR");
        ReflectionTestUtils.setField(commonProcess, "txt_REASON_CODE", "REASON_CODE");
        ReflectionTestUtils.setField(commonProcess, "isJsonInOutput", true);


        String actualOp = "\"{\\\"REQUESTRESPONSE\\\":{\\\"REQUEST\\\":{\\\"HEADER\\\":{\\\"REQ_TO\\\":\\\"JIMSYSTEM\\\",\\\"REQ_ID\\\":\\\"JIM_TEST\\\",\\\"REQ_SYS_SPECIFIC\\\":{\\\"CAM_SR_ID\\\":\\\"JIM_TEST\\\",\\\"REQUEST_TYPE\\\":\\\"JIM3REST\\\"},\\\"ERRORS\\\":{},\\\"REQ_FROM\\\":\\\"JIM5P\\\"},\\\"CONTENTS\\\":{\\\"KURUMA\\\":\\\"TTTMM0001\\\",\\\"API_TYPE\\\":\\\"getCConfiGurationData\\\",\\\"IDTRANS\\\":\\\"JIM5P20145894\\\",\\\"REQ_JSON\\\":\\\"{\\\\\\\"transactionID\\\\\\\":\\\\\\\"JIM5P20145894\\\\\\\",\\\\\\\"FINFamily\\\\\\\":\\\\\\\"TTT\\\\\\\",\\\\\\\"client_req_time\\\\\\\":\\\\\\\"2024-08-03 07:51:16.454\\\\\\\",\\\\\\\"CEUDATA\\\\\\\":[{\\\\\\\"CEUProptType\\\\\\\":\\\\\\\"UUUSD\\\\\\\",\\\\\\\"CEU_ADDRESS\\\\\\\":\\\\\\\"2324D\\\\\\\",\\\\\\\"CEU_configurationLink\\\\\\\":\\\\\\\"478544TY7B\\\\\\\"}]}\\\"}},\\\"RESPONSE\\\":\\\"{\\\\\\\"CONTENTS\\\\\\\":{\\\\\\\"FILED1\\\\\\\":\\\\\\\"4564564AAA\\\\\\\",\\\\\\\"FIELD2\\\\\\\":\\\\\\\"M000\\\\\\\",\\\\\\\"FIELD3\\\\\\\":\\\\\\\"The getCConfiGurationData process is successfully.\\\\\\\"},\\\\\\\"HEADER\\\\\\\":{\\\\\\\"ERRORS\\\\\\\":{\\\\\\\"ERROR\\\\\\\":[{\\\\\\\"RETURN_CODE\\\\\\\":\\\\\\\"00\\\\\\\",\\\\\\\"REASON_CODE\\\\\\\":\\\\\\\"DDDFFF000\\\\\\\"}]}}}\\\"}}\"";

        commonProcess.processOutput(actualOp, "", 4, false, "", "uuu");


    }


}
