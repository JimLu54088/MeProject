package jp.co.jim.controller;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.unitils.thirdparty.org.apache.commons.io.FileUtils;

import jp.co.jim.common.JwtTokenUtil;
import jp.co.jim.common.ZipUnzipUtil;
import jp.co.jim.service.LoginService;
import jp.co.jim.service.SearchResultService;
import lombok.RequiredArgsConstructor;

@SpringBootTest
@RequiredArgsConstructor
public class SearchByExcelControllerTest {


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

}
