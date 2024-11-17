package sQLqueriesGenerator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.*;
import org.mockito.*;
import org.unitils.thirdparty.org.apache.commons.io.FileUtils;

public class InsertQueryGeneratorTest {

	private static final String LOCAL_TEST_FOLDER = "D:\\myjava\\ttt";

	private static Path localTestFileDirPath;

	@BeforeClass
	public static void createTestDirectory() throws IOException {
		localTestFileDirPath = Files.createDirectories(FileSystems.getDefault().getPath(LOCAL_TEST_FOLDER));

	}

	@After
	public void deleteTempFileInEachTest() throws Exception {
		this.deleteFilesInFolder(LOCAL_TEST_FOLDER);
	}

	@AfterClass
	public static void removeTestDirectory() throws IOException {

		FileUtils.deleteDirectory(localTestFileDirPath.toFile());

	}

	@Test
	public void replaceSQNullSQToNull_test_5X5() throws Exception {

		// Create test sql file
		Files.deleteIfExists(localTestFileDirPath.resolve("SQNULLSQToNULL_test_5X5.sql"));

		Path sql_test_5X5_path = Files.createFile(localTestFileDirPath.resolve("SQNULLSQToNULL_test_5X5.sql"));

		try (BufferedWriter br = new BufferedWriter(
				new FileWriter(sql_test_5X5_path.toString(), StandardCharsets.UTF_8))) {

			br.write(
					"INSERT INTO `t_hist_operation` (`ROW_ID`, `USER_ID`, `USER_OPERATION_DETAILS`, `INS_BY`, `INS_DT`) VALUES ('8', 'abc', 'NULL', 'UserActionMonitor', '2024-06-30 11:36:33');");
			br.newLine(); // 換行

			// 寫入第二行數據
			br.write(
					"INSERT INTO `t_hist_operation` (`ROW_ID`, `USER_ID`, `USER_OPERATION_DETAILS`, `INS_BY`, `INS_DT`) VALUES ('NULL', 'abc', 'Admin Login successful', 'UserActionMonitor', '2024-06-30 12:11:31');");

		}

		InsertQueriesGeneratorImp imp = new InsertQueriesGeneratorImp();

		imp.replaceSQNullSQToNull(LOCAL_TEST_FOLDER, sql_test_5X5_path.toString());

		// Read outputSQL file

		Path outputSQLTestFile = Paths.get(localTestFileDirPath + "\\finalOutputSql.sql");

		String[] expectedLines = {
				"INSERT INTO `t_hist_operation` (`ROW_ID`, `USER_ID`, `USER_OPERATION_DETAILS`, `INS_BY`, `INS_DT`) VALUES ('8', 'abc', NULL, 'UserActionMonitor', '2024-06-30 11:36:33');",
				"INSERT INTO `t_hist_operation` (`ROW_ID`, `USER_ID`, `USER_OPERATION_DETAILS`, `INS_BY`, `INS_DT`) VALUES (NULL, 'abc', 'Admin Login successful', 'UserActionMonitor', '2024-06-30 12:11:31');", };

		try (BufferedReader reader = new BufferedReader(new FileReader(outputSQLTestFile.toString()))) {
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
	public void replaceSQNullSQToNull_test_5X5_fail() throws Exception {

		InsertQueriesGeneratorImp imp = new InsertQueriesGeneratorImp();

		try {
			imp.replaceSQNullSQToNull("", "");
		} catch (Exception ex) {

			assertEquals(FileNotFoundException.class, ex.getClass());

		}

	}

	@Test
	public void createInsertQuery_success() throws Exception {

		// Create test excel file
		Files.deleteIfExists(localTestFileDirPath.resolve("insertQuerytest.xlsx"));

		Path test_excelFile_path = Files.createFile(localTestFileDirPath.resolve("insertQuerytest.xlsx"));

		createTestExcelFile(test_excelFile_path);

		InsertQueriesGeneratorImp imp = new InsertQueriesGeneratorImp();

		Files.deleteIfExists(localTestFileDirPath.resolve("output_wrk.sql"));

		Path output_work_tempSQLfile = Files.createFile(localTestFileDirPath.resolve("output_wrk.sql"));

		String tempoutputFilePath = output_work_tempSQLfile.toString();

		// 模擬用戶輸入
		String simulatedInput = "t_hist_operation\n3\n";
		InputStream originalSystemIn = System.in;
		System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

		imp.createInsertQuery(String.valueOf(test_excelFile_path), tempoutputFilePath, 3);

		String[] expectedLines = {
				"INSERT INTO `t_hist_operation` (`ROW_ID`, `USER_ID`, `USER_OPERATION_DETAILS`) VALUES ('8', 'abc', 'Admin Login successful');",
				"INSERT INTO `t_hist_operation` (`ROW_ID`, `USER_ID`, `USER_OPERATION_DETAILS`) VALUES ('9', 'abc', 'Admin Login successful');",
				"INSERT INTO `t_hist_operation` (`ROW_ID`, `USER_ID`, `USER_OPERATION_DETAILS`) VALUES ('10', 'abc', 'Admin Login successful');" };

		try (BufferedReader reader = new BufferedReader(new FileReader(tempoutputFilePath))) {
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

		// 還原標準輸入和標準輸出
		System.setIn(originalSystemIn);

	}

	@Test
	public void main_test() throws Exception {

		// Create test excel file
		Files.deleteIfExists(localTestFileDirPath.resolve("insertQuerytest.xlsx"));

		Path test_excelFile_path = Files.createFile(localTestFileDirPath.resolve("insertQuerytest.xlsx"));

		createTestExcelFile(test_excelFile_path);

		String[] args = { test_excelFile_path.toString(), LOCAL_TEST_FOLDER, "3" };

		// 模擬用戶輸入
		String simulatedInput = "t_hist_operation\n3\n";
		InputStream originalSystemIn = System.in;
		System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

		InsertQueryGenerator.main(args);

		// 還原標準輸入和標準輸出
		System.setIn(originalSystemIn);

	}

	@Test
	public void main_args_null() throws Exception {

		try {
			InsertQueryGenerator.main(null);
		} catch (NullPointerException nullPointerException) {
			assertEquals(NullPointerException.class, nullPointerException.getClass());
		}

	}

	@Test
	public void InsertQueryGenerator_createInstanceToAddCoverage() throws Exception {
		InsertQueryGenerator insertQueryGeneratorInstance = new InsertQueryGenerator();

	}

	@Test
	public void case_outputSQLDirRootNotExist_thenCreateNewOne() throws Exception {

		// Create test excel file
		Files.deleteIfExists(localTestFileDirPath.resolve("insertQuerytest.xlsx"));

		Path test_excelFile_path = Files.createFile(localTestFileDirPath.resolve("insertQuerytest.xlsx"));

		createTestExcelFile(test_excelFile_path);

		String[] args = { test_excelFile_path.toString(), LOCAL_TEST_FOLDER + "\\sub1", "3" };

		// 模擬用戶輸入
		String simulatedInput = "t_hist_operation\n3\n";
		InputStream originalSystemIn = System.in;
		System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

		InsertQueryGenerator.main(args);

		// 還原標準輸入和標準輸出
		System.setIn(originalSystemIn);

		// finally delete sub1 folder
		Path outputSQLDirRoot = Paths.get(LOCAL_TEST_FOLDER + "\\sub1");
		FileUtils.deleteDirectory(outputSQLDirRoot.toFile());

	}

	@Test
	public void case_tempOutSQLFileDIDNTdelete() throws Exception {

		// Create test excel file
		Files.deleteIfExists(localTestFileDirPath.resolve("insertQuerytest.xlsx"));

		Path test_excelFile_path = Files.createFile(localTestFileDirPath.resolve("insertQuerytest.xlsx"));

		createTestExcelFile(test_excelFile_path);

		String[] args = { test_excelFile_path.toString(), LOCAL_TEST_FOLDER, "3" };

		// 模擬用戶輸入
		String simulatedInput = "t_hist_operation\n3\n";
		InputStream originalSystemIn = System.in;
		System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

		Path tempSQLCanNotDelete = localTestFileDirPath.resolve("output_wrk.sql");

		// LockTestFile
		FileOutputStream lockedFileStream = new FileOutputStream(tempSQLCanNotDelete.toFile());

		InsertQueryGenerator.main(args);

		// 還原標準輸入和標準輸出
		System.setIn(originalSystemIn);

		// Unlock stream
		lockedFileStream.close();
	}

	@Test
	public void case_blankInExcelFile() throws Exception {

		// Create test excel file
		Files.deleteIfExists(localTestFileDirPath.resolve("insertQuerytest.xlsx"));

		Path test_excelFile_path = Files.createFile(localTestFileDirPath.resolve("insertQuerytest.xlsx"));

		createTestExcelFileIncludingBlank(test_excelFile_path);

		setExcelAllCellsToString(test_excelFile_path);

		String[] args = { test_excelFile_path.toString(), LOCAL_TEST_FOLDER, "3" };

		// 模擬用戶輸入
		String simulatedInput = "t_hist_operation\n3\n";
		InputStream originalSystemIn = System.in;
		System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

		InsertQueryGenerator.main(args);

		// 還原標準輸入和標準輸出
		System.setIn(originalSystemIn);

	}

	// This test case is only to improve coverage.
	@Test
	public void case_integerInExcelFile() throws Exception {

		// Create test excel file
		Files.deleteIfExists(localTestFileDirPath.resolve("insertQuerytest.xlsx"));

		Path test_excelFile_path = Files.createFile(localTestFileDirPath.resolve("insertQuerytest.xlsx"));

		createTestExcelFileIncludingInteger(test_excelFile_path);

		setExcelAllCellsToString(test_excelFile_path);

		String[] args = { test_excelFile_path.toString(), LOCAL_TEST_FOLDER, "3" };

		// 模擬用戶輸入
		String simulatedInput = "t_hist_operation\n3\n";
		InputStream originalSystemIn = System.in;
		System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

		InsertQueryGenerator.main(args);

		// 還原標準輸入和標準輸出
		System.setIn(originalSystemIn);

	}

	@Test
	public void case_includingBlankInExcelFile() throws Exception {

		// Create test excel file
		Files.deleteIfExists(localTestFileDirPath.resolve("insertQuerytest.xlsx"));

		Path test_excelFile_path = Files.createFile(localTestFileDirPath.resolve("insertQuerytest.xlsx"));

		createTestExcelFileIncludingBlanK(test_excelFile_path);

//		setExcelAllCellsToString(test_excelFile_path);

		String[] args = { test_excelFile_path.toString(), LOCAL_TEST_FOLDER, "3" };

		// 模擬用戶輸入
		String simulatedInput = "t_hist_operation\n3\n";
		InputStream originalSystemIn = System.in;
		System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

		InsertQueryGenerator.main(args);

		// 還原標準輸入和標準輸出
		System.setIn(originalSystemIn);

	}

	public void deleteFilesInFolder(String folderPath) {

		try {

			AtomicInteger deletedFileCount = new AtomicInteger(0);

			Files.walkFileTree(Paths.get(folderPath), new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Files.delete(file);
					System.out.println("Deleted file: " + file);
					deletedFileCount.incrementAndGet();
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
					// Handle the exception
					System.out.println("Error occured.");
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					// Do nothing for directories
					return FileVisitResult.CONTINUE;
				}
			});
			System.out.println(deletedFileCount.get() + " files deleted successfully.");
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public void createTestExcelFile(Path test_excelFile_path) {
		// 創建工作簿
		try (Workbook workbook = new XSSFWorkbook()) {
			// 創建工作表
			Sheet sheet = workbook.createSheet("Sheet1");

			// 創建表頭
			Row headerRow = sheet.createRow(0);
			Cell headerCell1 = headerRow.createCell(0);
			headerCell1.setCellValue("ROW_ID");
			Cell headerCell2 = headerRow.createCell(1);
			headerCell2.setCellValue("USER_ID");
			Cell headerCell3 = headerRow.createCell(2);
			headerCell3.setCellValue("USER_OPERATION_DETAILS");

			// 創建數據行
			Object[][] data = { { "8", "abc", "Admin Login successful" }, { "9", "abc", "Admin Login successful" },
					{ "10", "abc", "Admin Login successful" } };

			int rowNum = 1;
			for (Object[] rowData : data) {
				Row row = ((Sheet) sheet).createRow(rowNum++);
				int colNum = 0;
				for (Object cellData : rowData) {
					Cell cell = row.createCell(colNum++);
					if (cellData instanceof Integer) {
						cell.setCellValue((Integer) cellData);
					} else if (cellData instanceof String) {
						cell.setCellValue((String) cellData);
					}
				}
			}

			// 寫入到文件
			try (FileOutputStream fileOut = new FileOutputStream(String.valueOf(test_excelFile_path))) {
				workbook.write(fileOut);
			}

			System.out.println("Excel file created successfully!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createTestExcelFileIncludingBlank(Path test_excelFile_path) {
		// 創建工作簿
		try (Workbook workbook = new XSSFWorkbook()) {
			// 創建工作表
			Sheet sheet = workbook.createSheet("Sheet1");

			// 創建表頭
			Row headerRow = sheet.createRow(0);
			Cell headerCell1 = headerRow.createCell(0);
			headerCell1.setCellValue("ROW_ID");
			Cell headerCell2 = headerRow.createCell(1);
			headerCell2.setCellValue("USER_ID");
			Cell headerCell3 = headerRow.createCell(2);
			headerCell3.setCellValue("USER_OPERATION_DETAILS");

			// 創建數據行
			Object[][] data = { { "8", "abc", "" }, { "9", "abc", "" }, { "10", "abc", "Admin Login successful" } };

			int rowNum = 1;
			for (Object[] rowData : data) {
				Row row = ((Sheet) sheet).createRow(rowNum++);
				int colNum = 0;
				for (Object cellData : rowData) {
					Cell cell = row.createCell(colNum++);
					if (cellData instanceof Integer) {
						cell.setCellValue((Integer) cellData);
					} else if (cellData instanceof String) {
						cell.setCellValue((String) cellData);
					}
				}
			}

			// 寫入到文件
			try (FileOutputStream fileOut = new FileOutputStream(String.valueOf(test_excelFile_path))) {
				workbook.write(fileOut);
			}

			System.out.println("Excel file created successfully!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createTestExcelFileIncludingInteger(Path test_excelFile_path) {
		// 創建工作簿
		try (Workbook workbook = new XSSFWorkbook()) {
			// 創建工作表
			Sheet sheet = workbook.createSheet("Sheet1");

			// 創建表頭
			Row headerRow = sheet.createRow(0);
			Cell headerCell1 = headerRow.createCell(0);
			headerCell1.setCellValue("ROW_ID");
			Cell headerCell2 = headerRow.createCell(1);
			headerCell2.setCellValue("USER_ID");
			Cell headerCell3 = headerRow.createCell(2);
			headerCell3.setCellValue("USER_OPERATION_DETAILS");

			// 創建數據行
			Object[][] data = { { 8, "abc", "" }, { "9", "abc", "" }, { "10", "abc", "Admin Login successful" } };

			int rowNum = 1;
			for (Object[] rowData : data) {
				Row row = ((Sheet) sheet).createRow(rowNum++);
				int colNum = 0;
				for (Object cellData : rowData) {
					Cell cell = row.createCell(colNum++);
					if (cellData instanceof Integer) {
						cell.setCellValue((Integer) cellData);
					} else if (cellData instanceof String) {
						cell.setCellValue((String) cellData);
					}
				}
			}

			// 寫入到文件
			try (FileOutputStream fileOut = new FileOutputStream(String.valueOf(test_excelFile_path))) {
				workbook.write(fileOut);
			}

			System.out.println("Excel file created successfully!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createTestExcelFileIncludingBlanK(Path test_excelFile_path) {
		// 創建工作簿
		try (Workbook workbook = new XSSFWorkbook()) {
			// 創建工作表
			Sheet sheet = workbook.createSheet("Sheet1");

			// 創建表頭
			Row headerRow = sheet.createRow(0);
			Cell headerCell1 = headerRow.createCell(0);
			headerCell1.setCellValue("ROW_ID");
			Cell headerCell2 = headerRow.createCell(1);
			headerCell2.setCellValue("USER_ID");
			Cell headerCell3 = headerRow.createCell(2);
			headerCell3.setCellValue("USER_OPERATION_DETAILS");

			// 創建數據行
			Object[][] data = { { "8", "abc" }, { "9", "abc" }, { "10", "abc", "Admin Login successful" } };

			int rowNum = 1;
			for (Object[] rowData : data) {
				Row row = ((Sheet) sheet).createRow(rowNum++);
				int colNum = 0;
				for (Object cellData : rowData) {
					Cell cell = row.createCell(colNum++);
					if (cellData instanceof Integer) {
						cell.setCellValue((Integer) cellData);
					} else if (cellData instanceof String) {
						cell.setCellValue((String) cellData);
					}
				}
			}

			// 寫入到文件
			try (FileOutputStream fileOut = new FileOutputStream(String.valueOf(test_excelFile_path))) {
				workbook.write(fileOut);
			}

			System.out.println("Excel file created successfully!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setExcelAllCellsToString(Path inputFilePath) throws Exception {
		try (FileInputStream fis = new FileInputStream(String.valueOf(inputFilePath));
				Workbook workbook = new XSSFWorkbook(fis)) {

			// 獲取 Sheet1
			Sheet sheet = workbook.getSheetAt(0);

			// 創建一個新的 CellStyle，並設置為文字格式
			CellStyle textStyle = workbook.createCellStyle();
			DataFormat format = workbook.createDataFormat();
			textStyle.setDataFormat(format.getFormat("@")); // "@" 表示文本格式

			// 設置每個單元格為文字格式
			for (Row row : sheet) {
				for (Cell cell : row) {
					cell.setCellStyle(textStyle);
				}
			}

			// 將變更保存到新的 Excel 文件
			try (FileOutputStream fos = new FileOutputStream(String.valueOf(inputFilePath))) {
				workbook.write(fos);
			}

			System.out.println("All cells in Sheet1 have been set to text format.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
