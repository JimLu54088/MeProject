package jp.co.jim.controller;

import static org.hamcrest.CoreMatchers.is;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertThat;
//import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jp.co.jim.common.JwtTokenUtil;
import jp.co.jim.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.WriterAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.unitils.thirdparty.org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RequiredArgsConstructor
public class LoginControllerTest {

    @Spy
    @InjectMocks
    private LoginController controller;


    @Mock
    private LoginService service;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    private AutoCloseable mockClose;

    private static Path localFileDirPath;
    private static Path uploadDirPath;


    private static final String MOCK_APPENDER = "MockAppender";


    private static final String MONEY_LOCAL_DIRECTORY_PATH = "D:\\shell_test_231103\\localtest\\";
    private static final String MONEY_LOCAL_UPLOAD_DIRECTORY_PATH = "D:\\uploadDirectory\\localtest\\";

    private static final String LOG_HEADER = "[" + LoginController.class.getSimpleName() + "] :: ";
    private static final String ERROR_LOG_HEADER = "[" + LoginController.class.getName() + "] :: ";

    @BeforeAll
    public static void createTestDirectory() throws IOException {
        localFileDirPath = Files.createDirectories(FileSystems.getDefault().getPath(MONEY_LOCAL_DIRECTORY_PATH));
        uploadDirPath = Files.createDirectories(FileSystems.getDefault().getPath(MONEY_LOCAL_UPLOAD_DIRECTORY_PATH));

    }

    @BeforeEach
    public void setUp() {

        mockClose = MockitoAnnotations.openMocks(this);
//        ReflectionTestUtils.setField(controller, "localDirectory", MONEY_LOCAL_DIRECTORY_PATH);
//        ReflectionTestUtils.setField(controller, "uploadDirectory", MONEY_LOCAL_UPLOAD_DIRECTORY_PATH);
//        ReflectionTestUtils.setField(controller, "targetDate", TEST_EXECUTE_DATE);

    }

    @AfterAll
    public static void removeTestDirectory() throws IOException {

//        FileUtils.deleteDirectory(localFileDirPath.toFile());
//        FileUtils.deleteDirectory(uploadDirPath.toFile());

    }

    @AfterEach
    public void tearDown() throws Exception {
        mockClose.close();
        removeAppender();

    }

    @Test
    public void successLogin() throws Exception {
        StringWriter writer = new StringWriter();
        addAppender(writer, MOCK_APPENDER, Level.INFO);

        Map<String, String> loginData = Map.of("username", "JJJ", "password", "123");

        when(service.checkAdminLogin(any(), any())).thenReturn(1);
        when(jwtTokenUtil.generateToken(any())).thenReturn("returnedToken");


        ResponseEntity<?> response = controller.login(loginData);

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        System.out.println(LOG_HEADER + "received requestBody for Login: " + jsonResponse);

        assertTrue(writer.toString().contains(LOG_HEADER + "received requestBody for Login:"));
        assertTrue(writer.toString().contains("\"username\":\"JJJ\""));
        assertTrue(writer.toString().contains("\"password\":\"123\""));
    }

    @Test
    public void failLogin() throws Exception{
        StringWriter writer = new StringWriter();
        addAppender(writer, MOCK_APPENDER, Level.INFO);

        Map<String, String> loginData = Map.of("username", "JJJ", "password", "123");

        when(service.checkAdminLogin(any(), any())).thenReturn(0);
        when(jwtTokenUtil.generateToken(any())).thenReturn("returnedToken");


        ResponseEntity<?> response = controller.login(loginData);

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        System.out.println(LOG_HEADER + "received requestBody for Login: " + jsonResponse);

        assertTrue(writer.toString().contains(LOG_HEADER + "received requestBody for Login:"));
        assertTrue(writer.toString().contains("\"username\":\"JJJ\""));
        assertTrue(writer.toString().contains("\"password\":\"123\""));



    }


    private void addAppender(StringWriter writer, String name, Level level) {
        final LoggerContext context = LoggerContext.getContext(false);
        final Configuration config = context.getConfiguration();
        final PatternLayout layout = PatternLayout.createDefaultLayout(config);

        Appender appender = WriterAppender.createAppender(layout, null, writer, name, false, true);
        appender.start();
        config.addAppender(appender);
        updateLoggers(appender, config, level);

    }

    private void updateLoggers(Appender appender, Configuration config, Level level) {
        for (final LoggerConfig loggerConfig : config.getLoggers().values()) {
            loggerConfig.addAppender(appender, level, null);

        }
        config.getRootLogger().addAppender(appender, level, null);
    }

    private void removeAppender() {
        final LoggerContext context = LoggerContext.getContext(false);
        final Configuration config = context.getConfiguration();

        for (final LoggerConfig loggerConfig : config.getLoggers().values()) {
            loggerConfig.removeAppender(MOCK_APPENDER);

        }
        config.getRootLogger().removeAppender(MOCK_APPENDER);

    }

    private void deleteTempFile() throws IOException {

        if (Files.exists(localFileDirPath)) {
            Files.newDirectoryStream(localFileDirPath).forEach(file -> {
                try {
                    Files.delete(file);

                } catch (IOException ioex) {
                    ioex.getMessage();
                }
            });
        }

        if (Files.exists(uploadDirPath)) {
            Files.newDirectoryStream(uploadDirPath).forEach(file -> {
                try {
                    Files.delete(file);

                } catch (IOException ioex) {
                    ioex.getMessage();
                }
            });
        }

    }


}
