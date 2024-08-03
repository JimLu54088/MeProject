package jp.co.jim;


import jp.co.jim.process.TestProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
public class RdsRequestRegressionTestBatchApplication {


    @Autowired
    private TestProcess testProcess;


    public static void main(String[] args) {
        SpringApplication.run(RdsRequestRegressionTestBatchApplication.class, args);
    }

    @Bean
    public String startProcess() throws IOException {
        System.out.println("================");
        testProcess.testExecution();
        return "";
    }

}
