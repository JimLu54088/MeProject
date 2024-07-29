package jp.co.jim.process;

import jp.co.jim.common.Constants;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

@Component("jp.co.jim.process.TestProcess")
public class TestProcess extends CommonProcess {

    private String button_name = "";


    public void testExecution() throws IOException {


        String todayDate = Constants.dateFormatYYYYMMDD.format(new Date());

        System.out.println("todayDate: " + todayDate);

        evidence_folder_path = rootDir + "\\" + environment + "\\" + todayDate + "\\" + "evidence_" + executeCounter + pathSuffix + beforeAfter;

        createDirIfNotExist(evidence_folder_path);

        String output_file_path = rootDir + "\\" + environment + "\\" + todayDate + "\\" + "output_" + executeCounter + pathSuffix + beforeAfter;

        createDirIfNotExist(output_file_path);

        test_cases_file_path = rootDir + "\\" + environment + "\\" + todayDate + "\\" + environment + excelNameMiddle + beforeAfter + ".xlsx";


        Path sourceExcelFile = Paths.get(rootDir + "\\" + environment + "\\" + SampleExcelFileName);
        Path targetFileExcelFile = Paths.get(test_cases_file_path);

        //SampleExcelFileName

        // 复制文件到目标目录
        try {
            Files.copy(sourceExcelFile, targetFileExcelFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }


        output_file_path = output_file_path + outputFileName;

        if (isVersion2) {
            button_name = button_name_SRDV02;
            //Non-Prod
            if (!isProd) {
                app_URL = appURLRDSV02_DEV;
                test_page_URL = testPageURLDEV;
                //Prod
            } else {
                app_URL = appURLRDSV02_PROD;
                test_page_URL = testPageURLPROD;


            }

        } else {
            button_name = button_name_Normal;
            //Non-Prod
            if (!isProd) {
                app_URL = testPageURLDEV;
                //Prod
            } else {
                app_URL = testPageURLPROD;


            }

        }

        test_cases_sheet_name = executeCounter;

        accessApp(app_URL);

        if (null == post) {
            getCurrentTime();

        }

        output_file_path = output_file_path.replace("{TIMESTAMP}", post);
        outputFileName = outputFileName.replace("{TIMESTAMP}", post);

        //Define the file where you want to write the console output
        String logFileName = evidence_folder_path + "\\" + "Console_log" + Constants.dateFormatYYYYMMDDHHMMSS.format(new Date()) + ".log";
        File f = new File(logFileName);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        //Create a FileWriter to write to the log file
        try (FileWriter writer = new FileWriter(logFileName, true);
             BufferedWriter bufferedWriter = new BufferedWriter(writer);
        ) {

            //Redirect System out to the log file
            System.setOut(new PrintStream(new FileOutputStream(logFileName, true)));
            System.setErr(new PrintStream(new FileOutputStream(logFileName, true)));

            System.out.println("Executing test cases file : " + test_cases_file_path);
            System.out.println("Executing Sheet :   " + test_cases_sheet_name);

            try {
                executeRSDRequestTest();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            //Reset System out and System error to teh console
            System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
            System.setErr(new PrintStream(new FileOutputStream(FileDescriptor.err)));
        }


    }

    public void executeRSDRequestTest() {

        login(DG_User_ID, DG_USER_PASSWORD);

        if (isVersion2) {
            //TBD
        }

        //Start reading test cases
        boolean readTestCasesResult = readTestCases();

        System.out.println("readTestCasesResult: " + readTestCasesResult);

        waitForms(threadSleepingWaiting);

        getDriver().quit();

//        driver = null;


    }


}
