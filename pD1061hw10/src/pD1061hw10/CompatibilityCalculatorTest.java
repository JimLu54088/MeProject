package pD1061hw10;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.unitils.thirdparty.org.apache.commons.io.FileUtils;

public class CompatibilityCalculatorTest {

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
	public void test_NoFileInInputFolder() throws Exception {

		// 保存原本的標準輸出
		PrintStream originalOut = System.out;

		try {
			// 創建一個 ByteArray 來接收輸出
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			System.setOut(new PrintStream(outputStream));

			// 呼叫被測試的方法
			String[] args = { LOCAL_TEST_FOLDER };

			CompatibilityCalculator.main(args);

			// 斷言標準輸出是否符合預期
			assertEquals("No matching files found." + System.lineSeparator(), outputStream.toString());
		} finally {
			// 還原標準輸出
			System.setOut(originalOut);
		}

	}

	@Test
	public void test_OneFileNormal() throws Exception {

		// 保存原本的標準輸出
		PrintStream originalOut = System.out;

		try {
			// 創建一個 ByteArray 來接收輸出
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			System.setOut(new PrintStream(outputStream));

			// Create test file
			createTest01Normal();

			// 呼叫被測試的方法
			String[] args = { LOCAL_TEST_FOLDER };

			CompatibilityCalculator.main(args);

			// 斷言標準輸出是否符合預期
			assertEquals("10000,1001,1002,10,40" + System.lineSeparator(), outputStream.toString());
		} finally {
			// 還原標準輸出
			System.setOut(originalOut);
		}

	}

	@Test
	public void test_twoFilesNormal() throws Exception {

		// 保存原本的標準輸出
		PrintStream originalOut = System.out;

		try {
			// 創建一個 ByteArray 來接收輸出
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			System.setOut(new PrintStream(outputStream));

			// Create test file
			createTest01Normal();
			createTest02Normal();

			// 呼叫被測試的方法
			String[] args = { LOCAL_TEST_FOLDER };

			CompatibilityCalculator.main(args);

			// 斷言標準輸出是否符合預期
			assertEquals("10000,1001,1002,10,40" + System.lineSeparator() + "10000,30,20,0,20" + System.lineSeparator(),
					outputStream.toString());

		} finally {
			// 還原標準輸出
			System.setOut(originalOut);
		}

	}

	@Test
	public void test_oneFileFirstLineIsNotInt() throws Exception {

		// 保存原本的標準輸出
		PrintStream originalOut = System.out;

		try {
			// 創建一個 ByteArray 來接收輸出
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			System.setOut(new PrintStream(outputStream));

			// Create test file
			createTest01FirstLineNotInt();
			createTest02Normal();

			// 呼叫被測試的方法
			String[] args = { LOCAL_TEST_FOLDER };

			CompatibilityCalculator.main(args);

			// 斷言標準輸出是否符合預期
			assertEquals("The first line of test01.txt is not a integer." + System.lineSeparator() + "10000,30,20,0,20"
					+ System.lineSeparator(), outputStream.toString());

		} finally {
			// 還原標準輸出
			System.setOut(originalOut);
		}

	}

	@Test
	public void test_inputFolderNotExist() throws Exception {

		// 保存原本的標準輸出
		PrintStream originalOut = System.out;

		try {
			// 創建一個 ByteArray 來接收輸出
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			System.setOut(new PrintStream(outputStream));

			// 呼叫被測試的方法
			String[] args = { LOCAL_TEST_FOLDER + "\\ttt8" };

			CompatibilityCalculator.main(args);

		} finally {
			// 還原標準輸出
			System.setOut(originalOut);
		}

	}

	@Test
	public void test_processFileIOE() throws Exception {

		// 保存原本的標準輸出
		PrintStream originalOut = System.out;

		try {
			// 創建一個 ByteArray 來接收輸出
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			System.setOut(new PrintStream(outputStream));

			// 呼叫被測試的方法
			String[] args = {};

			CompatibilityCalculator.processFile(LOCAL_TEST_FOLDER + "\\ttt003.txt");
		} finally {
			// 還原標準輸出
			System.setOut(originalOut);
		}

	}

	@Test
	public void test_ArgumentIsNull() throws Exception {

		// 保存原本的標準輸出
		PrintStream originalOut = System.out;

		try {
			// 創建一個 ByteArray 來接收輸出
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			System.setOut(new PrintStream(outputStream));

			// 呼叫被測試的方法
			String[] args = {};

			CompatibilityCalculator.main(args);

			// 斷言標準輸出是否符合預期
			assertEquals("Please set inputFolder Path." + System.lineSeparator(), outputStream.toString());
		} finally {
			// 還原標準輸出
			System.setOut(originalOut);
		}

	}

	// This test is to create each class instance so the coverage can be 100%
	@Test
	public void create_instance_test() throws Exception {

		CompatibilityCalculator instance = new CompatibilityCalculator();

	}

	public void createTest01Normal() {

		try {

			// Create test sql file
			Files.deleteIfExists(localTestFileDirPath.resolve("test01.txt"));

			Path test01Normal = Files.createFile(localTestFileDirPath.resolve("test01.txt"));

			try (BufferedWriter br = new BufferedWriter(
					new FileWriter(test01Normal.toString(), StandardCharsets.UTF_8))) {

				br.write("5");
				br.newLine(); // 換行

				br.write("This is");
				br.newLine(); // 換行
				br.write("This is an apple");
				br.newLine(); // 換行
				br.write("This is an apple. This is not a book.");
				br.newLine(); // 換行
				br.write("Oh yeah!!!!");
				br.newLine(); // 換行
				br.write("Today is his birthday");
				br.newLine(); // 換行
				br.write("This is");

			}

		}

		catch (Exception e) {
			e.printStackTrace();

		}
	}

	public void createTest01FirstLineNotInt() {

		try {

			// Create test sql file
			Files.deleteIfExists(localTestFileDirPath.resolve("test01.txt"));

			Path test01Normal = Files.createFile(localTestFileDirPath.resolve("test01.txt"));

			try (BufferedWriter br = new BufferedWriter(
					new FileWriter(test01Normal.toString(), StandardCharsets.UTF_8))) {

				br.write("kk");
				br.newLine(); // 換行

				br.write("This is");
				br.newLine(); // 換行
				br.write("This is an apple");
				br.newLine(); // 換行
				br.write("This is an apple. This is not a book.");
				br.newLine(); // 換行
				br.write("Oh yeah!!!!");
				br.newLine(); // 換行
				br.write("Today is his birthday");
				br.newLine(); // 換行
				br.write("This is");

			}

		}

		catch (Exception e) {
			e.printStackTrace();

		}
	}

	public void createTest02Normal() {

		try {

			// Create test sql file
			Files.deleteIfExists(localTestFileDirPath.resolve("test02.txt"));

			Path test01Normal = Files.createFile(localTestFileDirPath.resolve("test02.txt"));

			try (BufferedWriter br = new BufferedWriter(
					new FileWriter(test01Normal.toString(), StandardCharsets.UTF_8))) {

				br.write("5");
				br.newLine(); // 換行

				br.write("aaa cc");
				br.newLine(); // 換行
				br.write("aaacc");
				br.newLine(); // 換行
				br.write("ttaa");
				br.newLine(); // 換行
				br.write("ttss");
				br.newLine(); // 換行
				br.write("cca0");
				br.newLine(); // 換行
				br.write("aaa cc");

			}

		}

		catch (Exception e) {
			e.printStackTrace();

		}
	}

	public void deleteFilesInFolder(String folderPath) {

		try {

			AtomicInteger deletedFileCount = new AtomicInteger(0);

			Files.walkFileTree(Paths.get(folderPath), new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Files.delete(file);
//					System.out.println("Deleted file: " + file);
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
//			System.out.println(deletedFileCount.get() + " files deleted successfully.");
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
