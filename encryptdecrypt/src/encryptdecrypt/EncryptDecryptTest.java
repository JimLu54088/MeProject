package encryptdecrypt;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

public class EncryptDecryptTest {

	@Test
	public void encrypt_test() throws Exception {

		// 保存原本的標準輸出
		PrintStream originalOut = System.out;

		try {
			// 創建一個 ByteArray 來接收輸出
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			System.setOut(new PrintStream(outputStream));

			// 呼叫被測試的方法
			Encrypt.encrypt("tttuuui85885");

			// 斷言標準輸出是否符合預期
			assertEquals(
					"Original Message : tttuuui85885" + System.lineSeparator()
							+ "Encrypted Message : xccktX6lnlC0BRUgcSYPew==" + System.lineSeparator(),
					outputStream.toString());

		} finally {
			// 還原標準輸出
			System.setOut(originalOut);
		}

	}

	@Test
	public void decrypt_test() throws Exception {

		// 保存原本的標準輸出
		PrintStream originalOut = System.out;

		try {
			// 創建一個 ByteArray 來接收輸出
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			System.setOut(new PrintStream(outputStream));

			// 呼叫被測試的方法
			Decrypt.decrypt("xccktX6lnlC0BRUgcSYPew==");

			// 斷言標準輸出是否符合預期
			assertEquals("Encrypted Message : xccktX6lnlC0BRUgcSYPew==" + System.lineSeparator()
					+ "Decrypted Message : tttuuui85885" + System.lineSeparator(), outputStream.toString());

		} finally {
			// 還原標準輸出
			System.setOut(originalOut);
		}

	}

	@Test
	public void main_test_en() throws Exception {

		String[] args = { "Encrypt", "tttuuui85885" };
		// 保存原本的標準輸出
		PrintStream originalOut = System.out;

		try {
			// 創建一個 ByteArray 來接收輸出
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			System.setOut(new PrintStream(outputStream));

			// 呼叫被測試的方法
			EncryptDecryptExecutor.main(args);

			// 斷言標準輸出是否符合預期
			assertEquals(
					"Original Message : tttuuui85885" + System.lineSeparator()
							+ "Encrypted Message : xccktX6lnlC0BRUgcSYPew==" + System.lineSeparator(),
					outputStream.toString());

		} finally {
			// 還原標準輸出
			System.setOut(originalOut);
		}

	}

	@Test
	public void main_test_de() throws Exception {

		String[] args = { "Decrypt", "xccktX6lnlC0BRUgcSYPew==" };
		// 保存原本的標準輸出
		PrintStream originalOut = System.out;

		try {
			// 創建一個 ByteArray 來接收輸出
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			System.setOut(new PrintStream(outputStream));

			// 呼叫被測試的方法
			EncryptDecryptExecutor.main(args);

			// 斷言標準輸出是否符合預期
			assertEquals("Encrypted Message : xccktX6lnlC0BRUgcSYPew==" + System.lineSeparator()
					+ "Decrypted Message : tttuuui85885" + System.lineSeparator(), outputStream.toString());

		} finally {
			// 還原標準輸出
			System.setOut(originalOut);
		}

	}

	@Test
	public void main_test_doNothing() throws Exception {
		String[] args = { "Test", "xccktX6lnlC0BRUgcSYPew==" };

		// 呼叫被測試的方法
		EncryptDecryptExecutor.main(args);

	}

	@Test
	public void enctypt_fail() throws Exception {

		// 呼叫被測試的方法
		Encrypt.encrypt(null);

	}

	@Test
	public void dectypt_fail() throws Exception {

		// 呼叫被測試的方法
		Decrypt.decrypt(null);

	}

	// This test is to create each class instance so the coverage can be 100%
	@Test
	public void create_instance_test() throws Exception {

		EncryptDecryptExecutor encryptDecryptExecutor = new EncryptDecryptExecutor();
		Encrypt encrypt = new Encrypt();
		Decrypt decrypt = new Decrypt();
		EncryptAndDecryptUtil encryptAndDecryptUtil = new EncryptAndDecryptUtil();

	}

}
