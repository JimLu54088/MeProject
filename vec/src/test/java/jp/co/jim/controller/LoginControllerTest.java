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
import org.springframework.http.HttpHeaders;

import java.io.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jp.co.jim.common.JwtTokenUtil;
import jp.co.jim.common.ZipUnzipUtil;
import jp.co.jim.entity.ErrorDTO;
import jp.co.jim.entity.SearchCriteriaEntity;
import jp.co.jim.entity.SearchResultEntity;
import jp.co.jim.entity.WarningDTO;
import jp.co.jim.service.LoginService;
import jp.co.jim.service.SearchResultService;
import lombok.RequiredArgsConstructor;

import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.comparator.NameFileComparator;
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
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.unitils.thirdparty.org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.zip.ZipOutputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@RequiredArgsConstructor
public class LoginControllerTest {

	@Spy
	@InjectMocks
	private LoginController controller;

	@Mock
	private LoginService service;

	@Mock
	private SearchResultService searchResultService;

	@Mock
	private JwtTokenUtil jwtTokenUtil;

	private AutoCloseable mockClose;

	private static Path resultZipFileLocation_test;

	private static final String MOCK_APPENDER = "MockAppender";

	private static final String RESULT_ZIP_FILE_LOCATION = "D:\\shell_test_231103\\localtest\\vec_test\\result_zip_file_location\\";
	private static final String NotExistPath = "D:\\ppppppppppppppppppppppp"; // A path that not exist in PC
	private static final String LOG_HEADER = "[" + LoginController.class.getSimpleName() + "] :: ";
	private static final String ERROR_LOG_HEADER = "[" + LoginController.class.getName() + "] :: ";

	@BeforeAll
	public static void createTestDirectory() throws IOException {
		resultZipFileLocation_test = Files
				.createDirectories(FileSystems.getDefault().getPath(RESULT_ZIP_FILE_LOCATION));
	}

	@BeforeEach
	public void setUp() {

		mockClose = MockitoAnnotations.openMocks(this);
		ReflectionTestUtils.setField(controller, "resultZipFileLocation", RESULT_ZIP_FILE_LOCATION);
		ReflectionTestUtils.setField(controller, "maximum_result_count", 100000);
		ReflectionTestUtils.setField(controller, "maximum_save_search_criteria", 8);

		ReflectionTestUtils.setField(controller, "searchSingleKURCSVHeader",
				"KUR,PROJ_F_CODE,MODEL_CD,COLOR,MANUF_DATE");

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
	public void successLogin() throws Exception {
		StringWriter writer = new StringWriter();
		addAppender(writer, MOCK_APPENDER, Level.INFO);

		Map<String, String> loginData = null;

		loginData = Map.of("username", "JJJ", "password", "123");

		when(service.checkAdminLogin(any(), any())).thenReturn(1);
		when(jwtTokenUtil.generateToken(any())).thenReturn("returnedToken");

		ResponseEntity<?> response = controller.login(loginData);

		Gson gson = new Gson();
		String jsonResponse = gson.toJson(response);
		System.out.println(LOG_HEADER + "jsonResponsen: " + jsonResponse);

		assertTrue(writer.toString().contains(LOG_HEADER + "received requestBody for Login:"));
		assertTrue(writer.toString().contains("\"username\":\"JJJ\""));
		assertTrue(writer.toString().contains("\"password\":\"123\""));

		// Verify response
		assertEquals(HttpStatus.OK, response.getStatusCode());
		Map<String, String> responseBody = (Map<String, String>) response.getBody();
		assertEquals("returnedToken", responseBody.get("token"));

	}

	@Test
	public void failLogin() throws Exception {
		StringWriter writer = new StringWriter();
		addAppender(writer, MOCK_APPENDER, Level.INFO);

		Map<String, String> loginData = null;

		loginData = Map.of("username", "JJJ", "password", "123");
		when(service.checkAdminLogin(any(), any())).thenReturn(0);
		when(jwtTokenUtil.generateToken(any())).thenReturn("returnedToken");

		ResponseEntity<?> response = controller.login(loginData);

		Gson gson = new Gson();
		String jsonResponse = gson.toJson(response);
		System.out.println(LOG_HEADER + "jsonResponsen: " + jsonResponse);

		assertTrue(writer.toString().contains(LOG_HEADER + "received requestBody for Login:"));
		assertTrue(writer.toString().contains("\"username\":\"JJJ\""));
		assertTrue(writer.toString().contains("\"password\":\"123\""));

		// Verify response
		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
		assertEquals("Admin Login failed!", response.getBody());

	}

	@Test
	public void erroWhileCheckAdminLogin() throws Exception {
		StringWriter writer = new StringWriter();
		addAppender(writer, MOCK_APPENDER, Level.INFO);

		Map<String, String> loginData = null;

		loginData = Map.of("username", "JJJ", "password", "123");
		when(service.checkAdminLogin(any(), any())).thenThrow(new RuntimeException("Database Error has occured."));
		when(jwtTokenUtil.generateToken(any())).thenReturn("returnedToken");

		ResponseEntity<?> response = controller.login(loginData);

		Gson gson = new Gson();
		String jsonResponse = gson.toJson(response);
		System.out.println(LOG_HEADER + "jsonResponsen: " + jsonResponse);

		assertTrue(writer.toString().contains(ERROR_LOG_HEADER + "Error while checking admin login."));
		assertTrue(writer.toString().contains(LOG_HEADER + "received requestBody for Login:"));
		assertTrue(writer.toString().contains("\"username\":\"JJJ\""));
		assertTrue(writer.toString().contains("\"password\":\"123\""));

		// Verify response
		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
		assertEquals("Admin Login failed!", response.getBody());

	}

	@Test
	public void errorWhileGenerateToekn() throws Exception {
		StringWriter writer = new StringWriter();
		addAppender(writer, MOCK_APPENDER, Level.INFO);

		Map<String, String> loginData = null;

		loginData = Map.of("username", "JJJ", "password", "123");
		when(service.checkAdminLogin(any(), any())).thenReturn(1);
		when(jwtTokenUtil.generateToken(any())).thenThrow(new RuntimeException("Token getting failed."));

		ResponseEntity<?> response = null;
		Gson gson = new Gson();

		try {
			response = controller.login(loginData);
		} catch (Exception e) {

			String jsonResponse = gson.toJson(response);
			System.out.println(LOG_HEADER + "jsonResponsen: " + jsonResponse);

			assertTrue(writer.toString().contains(ERROR_LOG_HEADER + "Token getting failed."));
			assertTrue(writer.toString().contains(LOG_HEADER + "received requestBody for Login:"));
			assertTrue(writer.toString().contains("\"username\":\"JJJ\""));
			assertTrue(writer.toString().contains("\"password\":\"123\""));

		}

	}

	@Test
	public void searchSingleVecRequestParaEmpity() throws Exception {
		StringWriter writer = new StringWriter();
		addAppender(writer, MOCK_APPENDER, Level.INFO);

		Map<String, String> criteriaData = null;

		criteriaData = Map.of("kur", "", "project_jya_code", "", "model_code", "", "color", "", "manufacter_date", "",
				"userId", "JJJ");

		ResponseEntity<?> response = controller.searchSingleVec(criteriaData);

		Gson gson = new Gson();
		String jsonResponse = gson.toJson(response);
		System.out.println(LOG_HEADER + "jsonResponse: " + jsonResponse);

		assertTrue(writer.toString().contains(LOG_HEADER + "received requestBody for searchSingleVec:"));
		assertTrue(writer.toString().contains("\"manufacter_date\":\"\""));
		assertTrue(writer.toString().contains("\"color\":\"\""));
		assertTrue(writer.toString().contains("\"project_jya_code\":\"\""));
		assertTrue(writer.toString().contains("\"model_code\":\"\""));
		assertTrue(writer.toString().contains("\"userId\":\"JJJ\""));
		assertTrue(writer.toString().contains("\"kur\":\"\""));
		assertTrue(writer.toString().contains(LOG_HEADER + "No Criteria Specified."));

		// Verify response
		assertEquals(HttpStatus.OK, response.getStatusCode());

		WarningDTO responseBody = (WarningDTO) response.getBody();
		assertEquals("WSW002", responseBody.getWarningCode());
		assertEquals("No Criteria Specified.", responseBody.getWarningMessage());

	}

	@Test
	public void searchSingleVecRequestParaNULL() throws Exception {
		StringWriter writer = new StringWriter();
		addAppender(writer, MOCK_APPENDER, Level.INFO);

		Map<String, String> criteriaData = new HashMap<>();

		criteriaData.put("kur", null);
		criteriaData.put("project_jya_code", null);
		criteriaData.put("model_code", null);
		criteriaData.put("color", null);
		criteriaData.put("manufacter_date", null);
		criteriaData.put("userId", "JJJ");

		ResponseEntity<?> response = controller.searchSingleVec(criteriaData);

		Gson gson = new Gson();
		String jsonResponse = gson.toJson(response);
		System.out.println(LOG_HEADER + "jsonResponse: " + jsonResponse);

		assertTrue(writer.toString().contains(LOG_HEADER + "received requestBody for searchSingleVec:"));
		assertTrue(!writer.toString().contains("\"manufacter_date\":\"\""));
		assertTrue(!writer.toString().contains("\"color\":\"\""));
		assertTrue(!writer.toString().contains("\"project_jya_code\":\"\""));
		assertTrue(!writer.toString().contains("\"model_code\":\"\""));
		assertTrue(writer.toString().contains("\"userId\":\"JJJ\""));
		assertTrue(!writer.toString().contains("\"kur\":\"\""));
		assertTrue(writer.toString().contains(LOG_HEADER + "No Criteria Specified."));

		// Verify response
		assertEquals(HttpStatus.OK, response.getStatusCode());

		WarningDTO responseBody = (WarningDTO) response.getBody();
		assertEquals("WSW002", responseBody.getWarningCode());
		assertEquals("No Criteria Specified.", responseBody.getWarningMessage());

	}

	@Test
	public void searchSingleVecNoResultFound() throws Exception {
		StringWriter writer = new StringWriter();
		addAppender(writer, MOCK_APPENDER, Level.INFO);

		Map<String, String> criteriaData = null;

		criteriaData = Map.of("kur", "UUU", "project_jya_code", "", "model_code", "", "color", "", "manufacter_date",
				"", "userId", "JJJ");

		List<Map<String, Object>> resultList = new ArrayList<>();

		when(service.searchSingleVEC(any())).thenReturn(resultList);
		doNothing().when(searchResultService).saveSearchResultIntoDB(any());

		ResponseEntity<?> response = controller.searchSingleVec(criteriaData);

		Gson gson = new Gson();
		String jsonResponse = gson.toJson(response);
		System.out.println(LOG_HEADER + "jsonResponse: " + jsonResponse);

		assertTrue(writer.toString().contains(LOG_HEADER + "received requestBody for searchSingleVec:"));
		assertTrue(writer.toString().contains("\"manufacter_date\":\"\""));
		assertTrue(writer.toString().contains("\"color\":\"\""));
		assertTrue(writer.toString().contains("\"project_jya_code\":\"\""));
		assertTrue(writer.toString().contains("\"model_code\":\"\""));
		assertTrue(writer.toString().contains("\"userId\":\"JJJ\""));
		assertTrue(writer.toString().contains("\"kur\":\"UUU\""));
		assertTrue(writer.toString().contains(LOG_HEADER + "Search result count:: 0"));
		assertTrue(writer.toString().contains(LOG_HEADER + "No record found."));
		assertTrue(writer.toString().contains(LOG_HEADER + "Insert search result into DB."));

		// Verify response
		assertEquals(HttpStatus.OK, response.getStatusCode());

		WarningDTO responseBody = (WarningDTO) response.getBody();
		assertEquals("WSW001", responseBody.getWarningCode());
		assertEquals("No record found. Please change another criteria.", responseBody.getWarningMessage());

	}

	@Test
	public void erroWhilesaveSearchResultIntoDB_searchSingleVec() throws Exception {
		StringWriter writer = new StringWriter();
		addAppender(writer, MOCK_APPENDER, Level.INFO);

		Map<String, String> criteriaData = null;

		criteriaData = Map.of("kur", "UUU", "project_jya_code", "", "model_code", "", "color", "", "manufacter_date",
				"", "userId", "JJJ");

		doThrow(new RuntimeException("Database Error has occured.")).when(searchResultService)
				.saveSearchResultIntoDB(any());

		ResponseEntity<?> response = controller.searchSingleVec(criteriaData);

		Gson gson = new Gson();
		String jsonResponse = gson.toJson(response);
		System.out.println(LOG_HEADER + "jsonResponsen: " + jsonResponse);

		assertTrue(writer.toString().contains(LOG_HEADER + "received requestBody for searchSingleVec:"));
		assertTrue(writer.toString().contains("\"manufacter_date\":\"\""));
		assertTrue(writer.toString().contains("\"color\":\"\""));
		assertTrue(writer.toString().contains("\"project_jya_code\":\"\""));
		assertTrue(writer.toString().contains("\"model_code\":\"\""));
		assertTrue(writer.toString().contains("\"userId\":\"JJJ\""));
		assertTrue(writer.toString().contains("\"kur\":\"UUU\""));
		assertTrue(writer.toString().contains(LOG_HEADER + "Search result count:: 0"));
		assertTrue(writer.toString().contains(LOG_HEADER + "No record found."));
		assertTrue(writer.toString().contains(LOG_HEADER + "Insert search result into DB."));
		assertTrue(writer.toString().contains(ERROR_LOG_HEADER + "Error while insert search result into DB : "));

		// Verify response
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

		ErrorDTO responseBody = (ErrorDTO) response.getBody();
		assertEquals("WSE001", responseBody.getErrorCode());
		assertEquals("java.lang.RuntimeException: Database Error has occured.", responseBody.getErrorMessage());

	}

	@Test
	public void searchSingleVecResultCountExceedLimit() throws Exception {
		StringWriter writer = new StringWriter();
		addAppender(writer, MOCK_APPENDER, Level.INFO);

		Map<String, String> criteriaData = null;

		criteriaData = Map.of("kur", "UUU", "project_jya_code", "", "model_code", "", "color", "", "manufacter_date",
				"", "userId", "JJJ");

		List<Map<String, Object>> resultList = new ArrayList<>(100000);

		for (int i = 0; i < 100000; i++) {
			// 可以添加一個空的 HashMap 或隨便填一些數據
			Map<String, Object> map = new HashMap<>();
			resultList.add(map);
		}
		when(service.searchSingleVEC(any())).thenReturn(resultList);
		doNothing().when(searchResultService).saveSearchResultIntoDB(any());

		ResponseEntity<?> response = controller.searchSingleVec(criteriaData);

		Gson gson = new Gson();
		String jsonResponse = gson.toJson(response);
		System.out.println(LOG_HEADER + "jsonResponse: " + jsonResponse);

		assertTrue(writer.toString().contains(LOG_HEADER + "received requestBody for searchSingleVec:"));
		assertTrue(writer.toString().contains("\"manufacter_date\":\"\""));
		assertTrue(writer.toString().contains("\"color\":\"\""));
		assertTrue(writer.toString().contains("\"project_jya_code\":\"\""));
		assertTrue(writer.toString().contains("\"model_code\":\"\""));
		assertTrue(writer.toString().contains("\"userId\":\"JJJ\""));
		assertTrue(writer.toString().contains("\"kur\":\"UUU\""));
		assertTrue(writer.toString().contains(LOG_HEADER + "Search result count:: 100000"));
		assertTrue(writer.toString().contains(LOG_HEADER + "Insert search result into DB."));

		// Verify response
		assertEquals(HttpStatus.OK, response.getStatusCode());

		WarningDTO responseBody = (WarningDTO) response.getBody();
		assertEquals("WSW003", responseBody.getWarningCode());
		assertEquals("Count of search result exceeds limit :: 100000.", responseBody.getWarningMessage());

	}

	@Test
	public void Error_while_saving_rearch_result_searchSingleVecResultCountExceedLimit() throws Exception {
		StringWriter writer = new StringWriter();
		addAppender(writer, MOCK_APPENDER, Level.INFO);

		Map<String, String> criteriaData = null;

		criteriaData = Map.of("kur", "UUU", "project_jya_code", "", "model_code", "", "color", "", "manufacter_date",
				"", "userId", "JJJ");

		List<Map<String, Object>> resultList = new ArrayList<>(100000);

		for (int i = 0; i < 100000; i++) {
			// 可以添加一個空的 HashMap 或隨便填一些數據
			Map<String, Object> map = new HashMap<>();
			resultList.add(map);
		}
		when(service.searchSingleVEC(any())).thenReturn(resultList);
		doThrow(new RuntimeException("Database Error has occured.")).when(searchResultService)
				.saveSearchResultIntoDB(any());

		ResponseEntity<?> response = controller.searchSingleVec(criteriaData);

		Gson gson = new Gson();
		String jsonResponse = gson.toJson(response);
		System.out.println(LOG_HEADER + "jsonResponse: " + jsonResponse);

		assertTrue(writer.toString().contains(LOG_HEADER + "received requestBody for searchSingleVec:"));
		assertTrue(writer.toString().contains("\"manufacter_date\":\"\""));
		assertTrue(writer.toString().contains("\"color\":\"\""));
		assertTrue(writer.toString().contains("\"project_jya_code\":\"\""));
		assertTrue(writer.toString().contains("\"model_code\":\"\""));
		assertTrue(writer.toString().contains("\"userId\":\"JJJ\""));
		assertTrue(writer.toString().contains("\"kur\":\"UUU\""));
		assertTrue(writer.toString().contains(LOG_HEADER + "Search result count:: 100000"));
		assertTrue(writer.toString().contains(LOG_HEADER + "Insert search result into DB."));
		assertTrue(writer.toString().contains(ERROR_LOG_HEADER + "Error while insert search result into DB : "));

		// Verify response
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

		ErrorDTO responseBody = (ErrorDTO) response.getBody();
		assertEquals("WSE001", responseBody.getErrorCode());
		assertEquals("Database Error has occured.", responseBody.getErrorMessage());

	}

	@Test
	public void searchSingleVecSuccess() throws Exception {
		StringWriter writer = new StringWriter();
		addAppender(writer, MOCK_APPENDER, Level.INFO);

		Map<String, String> criteriaData = new HashMap<>();

		try {
			org.apache.commons.io.FileUtils.cleanDirectory(new File(RESULT_ZIP_FILE_LOCATION + "JJJ"));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		criteriaData.put("kur", "FFF");
		criteriaData.put("project_jya_code", "");
		criteriaData.put("model_code", "");
		criteriaData.put("color", "");
		criteriaData.put("manufacter_date", "");
		criteriaData.put("userId", "JJJ");

		List<Map<String, Object>> resultList = generateResultList1();

		when(service.searchSingleVEC(any())).thenReturn(resultList);

		ResponseEntity<?> response = controller.searchSingleVec(criteriaData);

		Gson gson = new Gson();
		String jsonResponse = gson.toJson(response);
		System.out.println(LOG_HEADER + "jsonResponse: " + jsonResponse);

		assertTrue(writer.toString().contains(LOG_HEADER + "received requestBody for searchSingleVec:"));
		assertTrue(writer.toString().contains("\"manufacter_date\":\"\""));
		assertTrue(writer.toString().contains("\"color\":\"\""));
		assertTrue(writer.toString().contains("\"project_jya_code\":\"\""));
		assertTrue(writer.toString().contains("\"model_code\":\"\""));
		assertTrue(writer.toString().contains("\"userId\":\"JJJ\""));
		assertTrue(writer.toString().contains("\"kur\":\"FFF\""));
		assertTrue(writer.toString().contains(LOG_HEADER + "Search result count:: 5"));
		assertTrue(writer.toString().contains(LOG_HEADER + "Insert search result into DB."));

		// Verify csv file

		List<File> zipFilesList = getFilteredFilesList(RESULT_ZIP_FILE_LOCATION + "JJJ", "*.zip", false, true);

		String extractedFolderPath = processZipFiles(zipFilesList);

		File extractedFolder = new File(extractedFolderPath);

		File[] targetCsvList = extractedFolder.listFiles();

		if (targetCsvList.length != 1) {
			fail("targetCsvList count is not 1");
		}

		File targetCSVFile = targetCsvList[0];

		String[] expectedLines = {
				"KUR,PROJ_F_CODE,MODEL_CD,COLOR,MANUF_DATE,EE Plnt,ser Plnt,TYC_RTY Plnt,REU_METER Plnt,UYU_RETER Plnt",
				"AAA,,,,Sep 15, 2024, 10:07:30 AM,,,,,", "DDD,,,,Sep 15, 2024, 10:07:19 AM,,,,,",
				"DJJ,DGGh,YYGF,Blue,Sep 11, 2024, 10:49:37 PM,,,772DF02XJ,20CX54J0A,",
				"EEE,,,,Sep 15, 2024, 10:07:39 AM,,,,,",
				"FFF,XXX,YYY,Red,Sep 10, 2024, 10:41:50 PM,,,,21CX54J0A,452DF02XJ" };

		try (BufferedReader reader = new BufferedReader(new FileReader(targetCSVFile))) {
			String line;
			int lineNumber = 0;

			while ((line = reader.readLine()) != null) {
				if (lineNumber >= expectedLines.length) {
					fail("More lines in the file than expected");
				}
				assertEquals(expectedLines[lineNumber], line);
				lineNumber++;
			}

			// 如果文件行数少于预期行数
			if (lineNumber < expectedLines.length) {
				fail("Fewer lines in the file than expected");
			}

		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException occurred: " + e.getMessage());
		}

		// Verify response
		assertEquals(HttpStatus.OK, response.getStatusCode());
		Map<String, String> responseBody = (Map<String, String>) response.getBody();

		// 需要測試的 URL
		String url = responseBody.get("downloadUrl");

		// 正則表達式：匹配 /api/download?fileName=d{8}-d{6}.zip&userId=JJJ
		String regex = "^/api/download\\?fileName=\\d{8}-\\d{6}\\.zip&userId=JJJ$";

		// 判斷 URL 是否匹配正則表達式
		boolean matches = Pattern.matches(regex, url);

		// 斷言結果
		assertTrue(matches, "URL does not match the expected pattern.");

	}

	@Test
	public void errorWhileInsertSearchResultIntoDB_searchSingleVEC_Success() throws Exception {
		StringWriter writer = new StringWriter();
		addAppender(writer, MOCK_APPENDER, Level.INFO);

		Map<String, String> criteriaData = new HashMap<>();

		try {
			org.apache.commons.io.FileUtils.cleanDirectory(new File(RESULT_ZIP_FILE_LOCATION + "JJJ"));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		criteriaData.put("kur", "FFF");
		criteriaData.put("project_jya_code", "");
		criteriaData.put("model_code", "");
		criteriaData.put("color", "");
		criteriaData.put("manufacter_date", "");
		criteriaData.put("userId", "JJJ");

		List<Map<String, Object>> resultList = generateResultList1();

		when(service.searchSingleVEC(any())).thenReturn(resultList);
		doThrow(new RuntimeException("Database Error has occured.")).when(searchResultService)
				.saveSearchResultIntoDB(any());

		ResponseEntity<?> response = controller.searchSingleVec(criteriaData);

		Gson gson = new Gson();
		String jsonResponse = gson.toJson(response);
		System.out.println(LOG_HEADER + "jsonResponse: " + jsonResponse);

		assertTrue(writer.toString().contains(LOG_HEADER + "received requestBody for searchSingleVec:"));
		assertTrue(writer.toString().contains("\"manufacter_date\":\"\""));
		assertTrue(writer.toString().contains("\"color\":\"\""));
		assertTrue(writer.toString().contains("\"project_jya_code\":\"\""));
		assertTrue(writer.toString().contains("\"model_code\":\"\""));
		assertTrue(writer.toString().contains("\"userId\":\"JJJ\""));
		assertTrue(writer.toString().contains("\"kur\":\"FFF\""));
		assertTrue(writer.toString().contains(LOG_HEADER + "Search result count:: 5"));
		assertTrue(writer.toString().contains(LOG_HEADER + "Insert search result into DB."));
		assertTrue(writer.toString().contains(ERROR_LOG_HEADER + "Error while insert search result into DB : "));

		// Verify response
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

		ErrorDTO responseBody = (ErrorDTO) response.getBody();
		assertEquals("WSE001", responseBody.getErrorCode());
		assertEquals("java.lang.RuntimeException: Database Error has occured.", responseBody.getErrorMessage());

		// Verify csv file

		List<File> zipFilesList = getFilteredFilesList(RESULT_ZIP_FILE_LOCATION + "JJJ", "*.zip", false, true);

		String extractedFolderPath = processZipFiles(zipFilesList);

		File extractedFolder = new File(extractedFolderPath);

		File[] targetCsvList = extractedFolder.listFiles();

		if (targetCsvList.length != 1) {
			fail("targetCsvList count is not 1");
		}

		File targetCSVFile = targetCsvList[0];

		String[] expectedLines = {
				"KUR,PROJ_F_CODE,MODEL_CD,COLOR,MANUF_DATE,EE Plnt,ser Plnt,TYC_RTY Plnt,REU_METER Plnt,UYU_RETER Plnt",
				"AAA,,,,Sep 15, 2024, 10:07:30 AM,,,,,", "DDD,,,,Sep 15, 2024, 10:07:19 AM,,,,,",
				"DJJ,DGGh,YYGF,Blue,Sep 11, 2024, 10:49:37 PM,,,772DF02XJ,20CX54J0A,",
				"EEE,,,,Sep 15, 2024, 10:07:39 AM,,,,,",
				"FFF,XXX,YYY,Red,Sep 10, 2024, 10:41:50 PM,,,,21CX54J0A,452DF02XJ" };

		try (BufferedReader reader = new BufferedReader(new FileReader(targetCSVFile))) {
			String line;
			int lineNumber = 0;

			while ((line = reader.readLine()) != null) {
				if (lineNumber >= expectedLines.length) {
					fail("More lines in the file than expected");
				}
				assertEquals(expectedLines[lineNumber], line);
				lineNumber++;
			}

			// 如果文件行数少于预期行数
			if (lineNumber < expectedLines.length) {
				fail("Fewer lines in the file than expected");
			}

		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException occurred: " + e.getMessage());
		}

	}

	@Test
	public void errorWhileSearch() throws Exception {
		StringWriter writer = new StringWriter();
		addAppender(writer, MOCK_APPENDER, Level.INFO);

		Map<String, String> criteriaData = new HashMap<>();

		try {
			org.apache.commons.io.FileUtils.cleanDirectory(new File(RESULT_ZIP_FILE_LOCATION + "JJJ"));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		criteriaData.put("kur", "FFF");
		criteriaData.put("project_jya_code", "");
		criteriaData.put("model_code", "");
		criteriaData.put("color", "");
		criteriaData.put("manufacter_date", "");
		criteriaData.put("userId", "JJJ");

		List<Map<String, Object>> resultList = generateResultList1();

		when(service.searchSingleVEC(any())).thenReturn(resultList);

		try (MockedStatic<Files> mockFiles = mockStatic(Files.class)) {

			// only模擬 Files.copy 拋出 IOException
			mockFiles.when(() -> Files.delete(any())).thenThrow(new IOException());

			ResponseEntity<?> response = controller.searchSingleVec(criteriaData);

			Gson gson = new Gson();
			String jsonResponse = gson.toJson(response);

			System.out.println(LOG_HEADER + "jsonResponse: " + jsonResponse);

			assertTrue(writer.toString().contains(LOG_HEADER + "received requestBody for searchSingleVec:"));
			assertTrue(writer.toString().contains("\"manufacter_date\":\"\""));
			assertTrue(writer.toString().contains("\"color\":\"\""));
			assertTrue(writer.toString().contains("\"project_jya_code\":\"\""));
			assertTrue(writer.toString().contains("\"model_code\":\"\""));
			assertTrue(writer.toString().contains("\"userId\":\"JJJ\""));
			assertTrue(writer.toString().contains("\"kur\":\"FFF\""));
			assertTrue(writer.toString().contains(LOG_HEADER + "Search result count:: 5"));
			assertTrue(!writer.toString().contains(LOG_HEADER + "Insert search result into DB."));
			assertTrue(writer.toString().contains(ERROR_LOG_HEADER + "Error while search single vec data from DB :"));

			// Verify response
			assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

			ErrorDTO responseBody = (ErrorDTO) response.getBody();
			assertEquals("WSE001", responseBody.getErrorCode());
			assertEquals(
					"java.lang.NullPointerException: Cannot invoke \"java.io.BufferedWriter.write(String)\" because \"writer\" is null",
					responseBody.getErrorMessage());

		} catch (Exception e) {

//Do nothing

		}

	}

	@Test
	public void saveSearchCriteriaExceedLimit() throws Exception {
		StringWriter writer = new StringWriter();
		addAppender(writer, MOCK_APPENDER, Level.INFO);

		Map<String, String> criteriaData = null;

		criteriaData = Map.of("kur", "UUU", "project_jya_code", "", "model_code", "", "color", "", "manufacter_date",
				"", "userId", "JJJ");

		when(service.countOfSavedSearchCriteriaByID(any())).thenReturn(8);

		ResponseEntity<?> response = controller.saveSearchCriteria(criteriaData);

		Gson gson = new Gson();
		String jsonResponse = gson.toJson(response);
		System.out.println(LOG_HEADER + "jsonResponse: " + jsonResponse);

		// Verify response
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

		ErrorDTO responseBody = (ErrorDTO) response.getBody();
		assertEquals("WSE002", responseBody.getErrorCode());
		assertEquals("Touch the limit of criteria saving. Cannot insert record anymore.",
				responseBody.getErrorMessage());

	}

	@Test
	public void saveSearchCriteriaSuccess() throws Exception {
		StringWriter writer = new StringWriter();
		addAppender(writer, MOCK_APPENDER, Level.INFO);

		Map<String, String> criteriaData = null;

		criteriaData = Map.of("kur", "UUU", "project_jya_code", "", "model_code", "", "color", "", "manufacter_date",
				"", "userId", "JJJ");

		when(service.countOfSavedSearchCriteriaByID(any())).thenReturn(6);
		doNothing().when(service).insertSearchCriteriaData(any());

		ResponseEntity<?> response = controller.saveSearchCriteria(criteriaData);

		Gson gson = new Gson();
		String jsonResponse = gson.toJson(response);
		System.out.println(LOG_HEADER + "jsonResponse: " + jsonResponse);

		// Verify response
		assertEquals(HttpStatus.OK, response.getStatusCode());
		Map<String, String> responseBody = (Map<String, String>) response.getBody();
		assertEquals("success", responseBody.get("success"));

	}

	@Test
	public void Error_while_saving_searchCriteriaIntoDB() throws Exception {
		StringWriter writer = new StringWriter();
		addAppender(writer, MOCK_APPENDER, Level.INFO);

		Map<String, String> criteriaData = null;

		criteriaData = Map.of("kur", "UUU", "project_jya_code", "", "model_code", "", "color", "", "manufacter_date",
				"", "userId", "JJJ");

		when(service.countOfSavedSearchCriteriaByID(any())).thenReturn(6);
		doThrow(new RuntimeException("Database Error has occured.")).when(service).insertSearchCriteriaData(any());

		ResponseEntity<?> response = controller.saveSearchCriteria(criteriaData);

		Gson gson = new Gson();
		String jsonResponse = gson.toJson(response);
		System.out.println(LOG_HEADER + "jsonResponse: " + jsonResponse);

		// Verify response
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

		ErrorDTO responseBody = (ErrorDTO) response.getBody();
		assertEquals("WSE001", responseBody.getErrorCode());
		assertEquals("java.lang.RuntimeException: Database Error has occured.", responseBody.getErrorMessage());

	}

	@Test
	public void getCriteriaListSuccess() throws Exception {
		StringWriter writer = new StringWriter();
		addAppender(writer, MOCK_APPENDER, Level.INFO);

		String user_id = "test";

		List<SearchCriteriaEntity> criteriaList = new ArrayList<SearchCriteriaEntity>();

		SearchCriteriaEntity entity = new SearchCriteriaEntity();
		entity.setKur("TTT");
		entity.setColor("Red");
		criteriaList.add(entity);

		when(service.selectCriteriaDataByID(any())).thenReturn(criteriaList);

		ResponseEntity<?> response = controller.getCriteriaList(user_id);

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
		SearchCriteriaEntity resultEntity = (SearchCriteriaEntity) responseBody.get(0);
		assertEquals("Red", resultEntity.getColor());
		assertEquals("TTT", resultEntity.getKur());
	}

	@Test
	public void getCriteriaListFail() throws Exception {
		StringWriter writer = new StringWriter();
		addAppender(writer, MOCK_APPENDER, Level.INFO);

		String user_id = "test";

		doThrow(new RuntimeException("Database Error has occured.")).when(service).selectCriteriaDataByID(any());

		ResponseEntity<?> response = controller.getCriteriaList(user_id);

		Gson gson = new Gson();
		String jsonResponse = gson.toJson(response);
		System.out.println(LOG_HEADER + "jsonResponse: " + jsonResponse);

		// Verify response
		// 檢查狀態碼
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

		// 檢查返回的錯誤訊息
		assertEquals("Error while getting saved criteria from DB!!", response.getBody());

	}

	@Test
	public void deleteSavedCriteriaSuccess() throws Exception {
		StringWriter writer = new StringWriter();
		addAppender(writer, MOCK_APPENDER, Level.INFO);

		String user_id = "test";
		String s_c_id = "s_c_id";

		doNothing().when(service).deleteSavedCriteriaByIDAndName(any(), any());

		ResponseEntity<?> response = controller.deleteSavedCriteria(user_id, s_c_id);

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
	public void deleteSavedCriteriaFail() throws Exception {
		StringWriter writer = new StringWriter();
		addAppender(writer, MOCK_APPENDER, Level.INFO);

		String user_id = "test";
		String s_c_id = "s_c_id";

		doThrow(new RuntimeException("Database Error has occured.")).when(service).deleteSavedCriteriaByIDAndName(any(),
				any());

		ResponseEntity<?> response = controller.deleteSavedCriteria(user_id, s_c_id);

		Gson gson = new Gson();
		String jsonResponse = gson.toJson(response);
		System.out.println(LOG_HEADER + "jsonResponse: " + jsonResponse);

		// Verify response
		// 檢查狀態碼是否為 INTERNAL_SERVER_ERROR
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

		// 檢查返回的錯誤訊息是否正確
		assertEquals("Error while delete Search CriteriaData from DB!!", response.getBody());

	}

	@Test
	public void downloadFileSuccess() throws Exception {
		StringWriter writer = new StringWriter();
		addAppender(writer, MOCK_APPENDER, Level.INFO);

		String user_id = "test";
		String fileName = "TEST.zip";

		Path filePath = Paths.get(RESULT_ZIP_FILE_LOCATION, user_id, fileName);

		// 模擬文件存在
		Files.createDirectories(filePath.getParent());
		Files.createFile(filePath);

		ResponseEntity<?> response = controller.downloadFile(fileName, user_id);

		// Verify response
		// 檢查狀態碼
		assertEquals(HttpStatus.OK, response.getStatusCode());

		// 檢查返回的 resource 類型
		assertTrue(response.getBody() instanceof FileSystemResource);

		// 檢查 Content-Type 是否為 APPLICATION_OCTET_STREAM
		assertEquals(MediaType.APPLICATION_OCTET_STREAM, response.getHeaders().getContentType());

		// 檢查 Content-Disposition header
		assertEquals("attachment; filename=\"" + fileName + "\"",
				response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));

		// 檢查 Content-Type 是否為 APPLICATION_OCTET_STREAM
		assertEquals(MediaType.APPLICATION_OCTET_STREAM, response.getHeaders().getContentType());

		// 清理測試文件
		Files.deleteIfExists(filePath);
		Files.delete(filePath.getParent());
	}

	@Test
	public void downloadFileFail() throws Exception {
		StringWriter writer = new StringWriter();
		addAppender(writer, MOCK_APPENDER, Level.INFO);

		String user_id = "test";
		String fileName = "TEST.zip";

		Path filePath = Paths.get(NotExistPath, user_id, fileName);

		ResponseEntity<?> response = controller.downloadFile(fileName, user_id);

		// Verify response
		// 檢查狀態碼是否為 404 Not Found
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
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

	private List<Map<String, Object>> generateResultList1() {

		// 創建 List<Map<String, Object>> 來存放結果
		List<Map<String, Object>> resultList = new ArrayList<>();

		// 第一個 Map
		Map<String, Object> map1 = new HashMap<>();
		map1.put("MANUF_DATE", "Sep 15, 2024, 10:07:30 AM");
		map1.put("EE Plnt", "");
		map1.put("KUR", "AAA");
		resultList.add(map1);

		// 第二個 Map
		Map<String, Object> map2 = new HashMap<>();
		map2.put("MANUF_DATE", "Sep 15, 2024, 10:07:19 AM");
		map2.put("KUR", "DDD");
		map2.put("ser Plnt", "");
		resultList.add(map2);

		// 第三個 Map
		Map<String, Object> map3 = new HashMap<>();
		map3.put("MANUF_DATE", "Sep 11, 2024, 10:49:37 PM");
		map3.put("TYC_RTY Plnt", "772DF02XJ");
		map3.put("REU_METER Plnt", "20CX54J0A");
		map3.put("COLOR", "Blue");
		map3.put("MODEL_CD", "YYGF");
		map3.put("PROJ_F_CODE", "DGGh");
		map3.put("KUR", "DJJ");
		resultList.add(map3);

		// 第四個 Map
		Map<String, Object> map4 = new HashMap<>();
		map4.put("MANUF_DATE", "Sep 15, 2024, 10:07:39 AM");
		map4.put("KUR", "EEE");
		resultList.add(map4);

		// 第五個 Map
		Map<String, Object> map5 = new HashMap<>();
		map5.put("MANUF_DATE", "Sep 10, 2024, 10:41:50 PM");
		map5.put("REU_METER Plnt", "21CX54J0A");
		map5.put("COLOR", "Red");
		map5.put("MODEL_CD", "YYY");
		map5.put("PROJ_F_CODE", "XXX");
		map5.put("UYU_RETER Plnt", "452DF02XJ");
		map5.put("KUR", "FFF");
		resultList.add(map5);

		return resultList;

	}

	public List<File> getFilteredFilesList(String baseDir, String fileFilterString, boolean searchSubDirs,
			boolean sortFiles) {

		List<File> filesList = null;

		boolean sortByName = true;

		WildcardFileFilter fileFilter = new WildcardFileFilter(fileFilterString.split(","), IOCase.INSENSITIVE);
		if (searchSubDirs) {
			filesList = new ArrayList<>(
					org.apache.commons.io.FileUtils.listFiles(new File(baseDir), fileFilter, TrueFileFilter.INSTANCE));
		} else {
			filesList = new ArrayList<>(org.apache.commons.io.FileUtils.listFiles(new File(baseDir), fileFilter, null));
		}

		if (sortFiles) {
			if (sortByName) {
				// Sorting files by name
				((NameFileComparator) NameFileComparator.NAME_INSENSITIVE_COMPARATOR).sort(filesList);
			} else {
				// Sorting files by lastmodified date
				((LastModifiedFileComparator) LastModifiedFileComparator.LASTMODIFIED_COMPARATOR).sort(filesList);
			}
		}

		return filesList;
	}

	public String processZipFiles(List<File> zipFilesList) {
		String baseDir = null;
		for (File zipFile : zipFilesList) {
			baseDir = getExtractFolder() + File.separator + zipFile.getName();
			unzipFile(zipFile, baseDir);

		}
		return baseDir;
	}

	public String getExtractFolder() {
		return RESULT_ZIP_FILE_LOCATION + "JJJ" + File.separator + "extract";
	}

	private void unzipFile(File zipFile, String targetDirectory) {
		System.out.println("Extracting file : " + zipFile.getName() + " into " + targetDirectory);
		try {
			FileUtils.forceMkdir(new File(targetDirectory));

			ZipUnzipUtil.unzipFile(zipFile.getAbsolutePath(), targetDirectory);

		} catch (Exception e) {

			System.out.println(String.format("File processing error.[%s]", "File extraction error", zipFile.getName()));

			e.printStackTrace();

		}

	}

	// private void deleteTempFile() throws IOException {
//
//		if (Files.exists(localFileDirPath)) {
//			Files.newDirectoryStream(localFileDirPath).forEach(file -> {
//				try {
//					Files.delete(file);
//
//				} catch (IOException ioex) {
//					ioex.getMessage();
//				}
//			});
//		}
//
//		if (Files.exists(uploadDirPath)) {
//			Files.newDirectoryStream(uploadDirPath).forEach(file -> {
//				try {
//					Files.delete(file);
//
//				} catch (IOException ioex) {
//					ioex.getMessage();
//				}
//			});
//		}
//
//	}

}
