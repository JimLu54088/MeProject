package jp.co.jim.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOCase;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.comparator.NameFileComparator;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.WriterAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.unitils.thirdparty.org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

import jp.co.jim.common.JwtTokenUtil;
import jp.co.jim.common.ZipUnzipUtil;
import jp.co.jim.entity.SearchCriteriaEntity;
import jp.co.jim.entity.SearchResultEntity;
import jp.co.jim.service.LoginService;
import jp.co.jim.service.SearchResultService;
import lombok.RequiredArgsConstructor;

@SpringBootTest
@RequiredArgsConstructor
public class ResultControllerTest {

	@Spy
	@InjectMocks
	private ResultController controller;

	@Mock
	private SearchResultService searchResultService;

	@Mock
	private JwtTokenUtil jwtTokenUtil;

	private AutoCloseable mockClose;

	private static Path resultZipFileLocation_test;

	private static final String MOCK_APPENDER = "MockAppender";

	private static final String RESULT_ZIP_FILE_LOCATION = "D:\\shell_test_231103\\localtest\\vec_test\\result_zip_file_location\\";
	private static final String LOG_HEADER = "[" + LoginController.class.getSimpleName() + "] :: ";

	@BeforeAll
	public static void createTestDirectory() throws IOException {
		resultZipFileLocation_test = Files
				.createDirectories(FileSystems.getDefault().getPath(RESULT_ZIP_FILE_LOCATION));
	}

	@BeforeEach
	public void setUp() {

		mockClose = MockitoAnnotations.openMocks(this);
		ReflectionTestUtils.setField(controller, "resultZipFileLocation", RESULT_ZIP_FILE_LOCATION);

	}

	@AfterAll
	public static void removeTestDirectory() throws IOException {

		FileUtils.deleteDirectory(resultZipFileLocation_test.toFile());

	}

	@AfterEach
	public void tearDown() throws Exception {
		mockClose.close();
		removeAppender();
		org.apache.commons.io.FileUtils.cleanDirectory(new File(RESULT_ZIP_FILE_LOCATION));

	}

	@Test
	public void getSearchResultList_success() throws Exception {
		StringWriter writer = new StringWriter();
		addAppender(writer, MOCK_APPENDER, Level.INFO);

		String user_id = "test";

		List<SearchResultEntity> searchResultList = new ArrayList<SearchResultEntity>();

		SearchResultEntity entity = new SearchResultEntity();
		entity.setS_r_id("ttt");
		entity.setUser_id("kkk");
		entity.setIns_by("YYY");
		searchResultList.add(entity);

		when(searchResultService.selectSearchResultByID(any())).thenReturn(searchResultList);

		ResponseEntity<?> response = controller.getSearchResultList(user_id);

		Gson gson = new Gson();
		String jsonResponse = gson.toJson(response);
		System.out.println(LOG_HEADER + "jsonResponse: " + jsonResponse);

		// Verify response
		// 檢查狀態碼
		assertEquals(HttpStatus.OK, response.getStatusCode());

		// 檢查返回的 body 是否是 List
		assertTrue(response.getBody() instanceof List<?>);

		// 檢查返回的列表是否包含期望的數據
		List<?> responseBody = (List<?>) response.getBody();
		assertEquals(1, responseBody.size());

		// 驗證返回的數據
		SearchResultEntity resultEntity = (SearchResultEntity) responseBody.get(0);
		assertEquals("ttt", resultEntity.getS_r_id());
		assertEquals("kkk", resultEntity.getUser_id());
		assertEquals("YYY", resultEntity.getIns_by());

	}

	@Test
	public void getSearchResultListFail() throws Exception {
		StringWriter writer = new StringWriter();
		addAppender(writer, MOCK_APPENDER, Level.INFO);

		String user_id = "test";

		doThrow(new RuntimeException("Database Error has occured.")).when(searchResultService)
				.selectSearchResultByID(any());

		ResponseEntity<?> response = controller.getSearchResultList(user_id);

		Gson gson = new Gson();
		String jsonResponse = gson.toJson(response);
		System.out.println(LOG_HEADER + "jsonResponse: " + jsonResponse);

		// Verify response
		// 檢查狀態碼
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

		// 檢查返回的錯誤訊息
		assertEquals("Error while getting search result from DB!!", response.getBody());

	}

	@Test
	public void deleteSavedsearchResult_success() throws Exception {
		StringWriter writer = new StringWriter();
		addAppender(writer, MOCK_APPENDER, Level.INFO);

		Map<String, String> requestDeleteBody = null;

		requestDeleteBody = Map.of("user_id", "JJJ", "s_r_id", "123", "ins_dt", "kkk");

		doNothing().when(searchResultService).deleteSavedsearchResult(any(), any(), any());

		ResponseEntity<?> response = controller.deleteSavedsearchResult(requestDeleteBody);

		Gson gson = new Gson();
		String jsonResponse = gson.toJson(response);
		System.out.println(LOG_HEADER + "jsonResponse: " + jsonResponse);

		// Verify response
		// 檢查狀態碼是否為 OK
		assertEquals(HttpStatus.OK, response.getStatusCode());

		// 檢查返回的 body 是否正確
		assertEquals("{\"status\": \"ok\"}", response.getBody());
	}

	@Test
	public void deleteSavedsearchResultFail() throws Exception {
		StringWriter writer = new StringWriter();
		addAppender(writer, MOCK_APPENDER, Level.INFO);

		Map<String, String> requestDeleteBody = null;

		requestDeleteBody = Map.of("user_id", "JJJ", "s_r_id", "123", "ins_dt", "kkk");

		doThrow(new RuntimeException("Database Error has occured.")).when(searchResultService)
				.deleteSavedsearchResult(any(), any(), any());

		ResponseEntity<?> response = controller.deleteSavedsearchResult(requestDeleteBody);

		Gson gson = new Gson();
		String jsonResponse = gson.toJson(response);
		System.out.println(LOG_HEADER + "jsonResponse: " + jsonResponse);

		// Verify response
		// 檢查狀態碼是否為 INTERNAL_SERVER_ERROR
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

		// 檢查返回的錯誤訊息是否正確
		assertEquals("Error while delete Saved search result from DB!!", response.getBody());

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



}
