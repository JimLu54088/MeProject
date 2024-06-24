package propertyTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.AfterClass;
import org.junit.Test;

import propertyTemplate.common.PropertyLoader;

public class TestMain {

	private static Path pockageDirPath;

	@AfterClass
	public static void removeTestPropertyFile() throws IOException {

		Path propertyfilepath = Paths.get(pockageDirPath + "\\" + "PropertyTest.properties");
		File propertyfile = propertyfilepath.toFile();

		propertyfile.delete();

	}

	@Test
	public void main_test() throws Exception {

		// 取得目前這個 class 的 Class 對象
		Class<?> clazz = TestMain.class;

		// 取得 class 的資源 URL
		URL resource = clazz.getResource(clazz.getSimpleName() + ".class");

		// 如果資源 URL 不為 null
		if (resource != null) {
			try {
				// 使用 URL 的 toURI 方法來取得 URI
				Path classFilePath = Paths.get(resource.toURI());
				// 取得 class 文件所在的目錄 的主包位置
				pockageDirPath = classFilePath.getParent().getParent().getParent();
				System.out.println("Class 所在目錄的絕對路徑: " + pockageDirPath);
			} catch (URISyntaxException e) {
				System.out.println("無法將 URL 轉換為 URI: " + e.getMessage());
			}
		} else {
			System.out.println("無法取得 class 資源 URL");
		}

		Files.deleteIfExists(pockageDirPath.resolve("PropertyTest.properties"));

		Path propertyfilepath = Paths.get(pockageDirPath + "\\" + "PropertyTest.properties");

		try (BufferedWriter br = new BufferedWriter(
				new FileWriter(propertyfilepath.toString(), StandardCharsets.UTF_8))) {

			br.write("abc=117");
			br.newLine(); // 換行

			// 寫入第二行數據
			br.write("cde=5");
			br.close();

		}

		String strpropertyfilepath = propertyfilepath.toString();

		String modifiedstrpropertyfilepath = strpropertyfilepath.replace("\\", "\\\\");

		String[] args = { modifiedstrpropertyfilepath };
		PrintStream originalOut = System.out;

		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			System.setOut(new PrintStream(outputStream));

			Main.main(args);

			assertEquals(
					"----------Process started ------------" + System.lineSeparator() + modifiedstrpropertyfilepath
							+ System.lineSeparator() + "abc's value : 117" + System.lineSeparator()
							+ "----------Process finished ------------" + System.lineSeparator(),
					outputStream.toString());

		} finally {
			System.setOut(originalOut);

		}

	}

	// This test is to create each class instance so the coverage can be 100%
	@Test
	public void create_instance_test() throws Exception {

		Main main = new Main();
		PropertyLoader propertyLoader = new PropertyLoader();

	}

	@Test
	public void input_null() throws Exception {
		// 设置自定义 SecurityManager
		SecurityManager originalSecurityManager = System.getSecurityManager();
		System.setSecurityManager(new NoExitSecurityManager());

		// 捕获 System.err 输出
		ByteArrayOutputStream errContent = new ByteArrayOutputStream();
		PrintStream originalErr = System.err;
		System.setErr(new PrintStream(errContent));

		try {
			// 运行主方法
			String[] args = {};
			Main.main(args);
			fail("Expected SecurityException not thrown");
		} catch (SecurityException e) {
			// 检查错误信息
			String expectedErrorMessage = "ERROR: Property File Name is not given. Please provide info -Dproperty.file=<Path of property file>";
			String actualErrorMessage = errContent.toString().trim();
			assertTrue(actualErrorMessage.contains(expectedErrorMessage));
			assertEquals("System.exit attempted with status: -1", e.getMessage());
		} finally {
			// 恢复原来的 SecurityManager
			System.setSecurityManager(originalSecurityManager);
			// 恢复原来的 System.err
			System.setErr(originalErr);
		}
	}

}
