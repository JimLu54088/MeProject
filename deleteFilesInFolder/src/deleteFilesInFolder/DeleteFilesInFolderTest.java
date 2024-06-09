package deleteFilesInFolder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.unitils.thirdparty.org.apache.commons.io.FileUtils;

public class DeleteFilesInFolderTest {

	private static Path localFileDirPath;
	private static Path localFileDirPathSub1;

	private static final String FOR_TEST_LOCAL_DIRECTORY_PATH = "D:\\test_files\\NMLroot\\test\\";
	private static final String FOR_TEST_LOCAL_DIRECTORY_PATH_SUB1 = "D:\\test_files\\NMLroot\\test\\testSub1\\";

	@BeforeClass
	public static void createTestDirectory() throws IOException {
		localFileDirPath = Files.createDirectories(FileSystems.getDefault().getPath(FOR_TEST_LOCAL_DIRECTORY_PATH));
		localFileDirPathSub1 = Files
				.createDirectories(FileSystems.getDefault().getPath(FOR_TEST_LOCAL_DIRECTORY_PATH_SUB1));

	}

	@AfterClass
	public static void removeTestDirectory() throws IOException {

		FileUtils.deleteDirectory(localFileDirPath.toFile());

	}

	@Test
	public void main_test_deleteSuccessfully() throws Exception {
		String[] args = { FOR_TEST_LOCAL_DIRECTORY_PATH };

		Files.deleteIfExists(localFileDirPath.resolve("App.log"));
		Files.deleteIfExists(localFileDirPath.resolve("App.log20240403"));
		Files.deleteIfExists(localFileDirPath.resolve("App.log20240402"));
		Files.deleteIfExists(localFileDirPathSub1.resolve("App.log20240402"));

		Files.createFile(localFileDirPath.resolve("App.log"));
		Files.createFile(localFileDirPath.resolve("App.log20240403"));
		Files.createFile(localFileDirPath.resolve("App.log20240402"));
		Files.createFile(localFileDirPathSub1.resolve("App.log20240402"));

		// 保存原本的標準輸入
		PrintStream originalOut = System.out;

		try {
			// 重定向標準輸出
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			System.setOut(new PrintStream(outputStream));

			// 呼叫被測試的方法
			DeleteFilesInFolder.main(args);

			// 斷言標準輸出是否符合預期
			assertEquals(
					"Deleted file: D:\\test_files\\NMLroot\\test\\App.log" + System.lineSeparator()
							+ "Deleted file: D:\\test_files\\NMLroot\\test\\App.log20240402" + System.lineSeparator()
							+ "Deleted file: D:\\test_files\\NMLroot\\test\\App.log20240403" + System.lineSeparator()
							+ "Deleted file: D:\\test_files\\NMLroot\\test\\testSub1\\App.log20240402"
							+ System.lineSeparator() + "4 files deleted successfully." + System.lineSeparator(),
					outputStream.toString());

			// Confirm folders didn't be deleted.
			Path folderPath_root = Paths.get(FOR_TEST_LOCAL_DIRECTORY_PATH);
			Path folderPath_root_sub1 = Paths.get(FOR_TEST_LOCAL_DIRECTORY_PATH_SUB1);

			assertTrue(Files.exists(folderPath_root));
			assertTrue(Files.exists(folderPath_root_sub1));

		} finally {

			// 還原標準輸入和輸出
			System.setOut(originalOut);
		}
	}

	@Test
	public void main_test_deleteFailed() throws Exception {
		String[] args = { FOR_TEST_LOCAL_DIRECTORY_PATH };

		Files.deleteIfExists(localFileDirPath.resolve("App.log"));

		Path fileDeleteFailed = Files.createFile(localFileDirPath.resolve("App.log"));

		// LockTestFile
		FileOutputStream lockedFileStream = new FileOutputStream(fileDeleteFailed.toFile());

//		Files.setPosixFilePermissions(fileDeleteFailed, PosixFilePermissions.fromString("r--r--r--"));

		// 保存原本的標準輸入
		PrintStream originalOut = System.out;

		try {
			// 重定向標準輸出
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			System.setOut(new PrintStream(outputStream));

			// 呼叫被測試的方法
			DeleteFilesInFolder.main(args);

			// 斷言標準輸出是否符合預期

			String consoleOutput = outputStream.toString().trim();
			// Assert that the message contains the expected text
			assertTrue(consoleOutput.contains("プロセスはファイルにアクセスできません。別のプロセスが使用中です。"));

		} catch (Exception ex) {
			//

		} finally {

			if (lockedFileStream != null) {
				lockedFileStream.close();
			}

			// 還原標準輸入和輸出
			System.setOut(originalOut);
		}
	}

	@Test
	public void main_test_deleteFailed_argsIsNull() throws Exception {
		String[] args = null;

		try {

			// 呼叫被測試的方法
			DeleteFilesInFolder.main(args);

		} catch (Exception ex) {
			Throwable throwable = ex.getCause();

			assertEquals(NullPointerException.class, throwable.getClass());

		} finally {

		}
	}

	// This test is to create each class instance so the coverage can be 100%
	@Test
	public void create_instance_test() throws Exception {

		DeleteFilesInFolder createdInstance = new DeleteFilesInFolder();

	}

}
