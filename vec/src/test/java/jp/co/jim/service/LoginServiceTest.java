package jp.co.jim.service;

import jp.co.jim.repository.LoginRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class LoginServiceTest {

    @Autowired
    private LoginService loginService;

    @Autowired
    private LoginRepository loginRepository; // 使用实际的Repository

//    private static final LocalDate TEST_EXECUTE_DATE_FROM = LocalDate.of(2023, 11, 05);
//    private static final LocalDate TEST_EXECUTE_DATE_TO = LocalDate.of(2023, 11, 06);

    @Test
    public void testSelectMoneyData() {


        int count= loginService.checkAdminLogin("FFF", "123");

        assertEquals(count, 1);


    }



}
