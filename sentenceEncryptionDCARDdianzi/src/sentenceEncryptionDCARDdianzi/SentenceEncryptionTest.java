package sentenceEncryptionDCARDdianzi;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.Test;

public class SentenceEncryptionTest {

	@Test
	public void testSentenceEncryption_return_ERROR() {

		// 保存原本的標準輸入
		InputStream originalSystemIn = System.in;
		PrintStream originalSystemOut = System.out;

		try {
			// 準備模擬的輸入
			ByteArrayInputStream mockInput = new ByteArrayInputStream("My name IS \"Jack\"".getBytes());
			System.setIn(mockInput);

			// 重定向標準輸出
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			System.setOut(new PrintStream(outputStream));

			// 呼叫被測試的方法
			SentenceEncryption.main(null);

			// 斷言標準輸出是否符合預期
			assertEquals("Please enter a sentence to encrypt:" + System.lineSeparator() + "Error. Please re-enter."
					+ System.lineSeparator(), outputStream.toString());
		} finally {
			// 還原標準輸入和輸出
			System.setIn(originalSystemIn);
			System.setOut(originalSystemOut);
		}
	}

	@Test
	public void testSentenceEncryption_case1() {

		// 保存原本的標準輸入
		InputStream originalSystemIn = System.in;
		PrintStream originalSystemOut = System.out;

		try {
			// 準備模擬的輸入
			ByteArrayInputStream mockInput = new ByteArrayInputStream("I want a FACE Mask.".getBytes());
			System.setIn(mockInput);

			// 重定向標準輸出
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			System.setOut(new PrintStream(outputStream));

			// 呼叫被測試的方法
			SentenceEncryption.main(null);

			// 斷言標準輸出是否符合預期
			assertEquals(
					"Please enter a sentence to encrypt:" + System.lineSeparator()
							+ "Encrypted sentence: i TNAW A ecaf KSAm." + System.lineSeparator(),
					outputStream.toString());
		} finally {
			// 還原標準輸入和輸出
			System.setIn(originalSystemIn);
			System.setOut(originalSystemOut);
		}
	}

	@Test
	public void testSentenceEncryption_case2() {

		// 保存原本的標準輸入
		InputStream originalSystemIn = System.in;
		PrintStream originalSystemOut = System.out;

		try {
			// 準備模擬的輸入
			ByteArrayInputStream mockInput = new ByteArrayInputStream("AZ COVID19 vaccine.".getBytes());
			System.setIn(mockInput);

			// 重定向標準輸出
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			System.setOut(new PrintStream(outputStream));

			// 呼叫被測試的方法
			SentenceEncryption.main(null);

			// 斷言標準輸出是否符合預期
			assertEquals(
					"Please enter a sentence to encrypt:" + System.lineSeparator()
							+ "Encrypted sentence: za 91divoc ENICCAV." + System.lineSeparator(),
					outputStream.toString());
		} finally {
			// 還原標準輸入和輸出
			System.setIn(originalSystemIn);
			System.setOut(originalSystemOut);
		}
	}

	@Test
	public void testSentenceEncryption_case_noPeriod() {

		// 保存原本的標準輸入
		InputStream originalSystemIn = System.in;
		PrintStream originalSystemOut = System.out;

		try {
			// 準備模擬的輸入
			ByteArrayInputStream mockInput = new ByteArrayInputStream("AZ COVID19 vaccine".getBytes());
			System.setIn(mockInput);

			// 重定向標準輸出
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			System.setOut(new PrintStream(outputStream));

			// 呼叫被測試的方法
			SentenceEncryption.main(null);

			// 斷言標準輸出是否符合預期
			assertEquals(
					"Please enter a sentence to encrypt:" + System.lineSeparator()
							+ "Encrypted sentence: za 91divoc ENICCAV" + System.lineSeparator(),
					outputStream.toString());
		} finally {
			// 還原標準輸入和輸出
			System.setIn(originalSystemIn);
			System.setOut(originalSystemOut);
		}
	}

	@Test
	public void testSentenceEncryption_coverage100() {

		// 保存原本的標準輸入
		InputStream originalSystemIn = System.in;
		PrintStream originalSystemOut = System.out;

		try {
			// 準備模擬的輸入
			ByteArrayInputStream mockInput = new ByteArrayInputStream("AZ COVID19 vaccine".getBytes());
			System.setIn(mockInput);

			// 重定向標準輸出
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			System.setOut(new PrintStream(outputStream));

			// 呼叫被測試的方法
			SentenceEncryption sen = new SentenceEncryption();
			sen.main(null);

			// 斷言標準輸出是否符合預期
			assertEquals(
					"Please enter a sentence to encrypt:" + System.lineSeparator()
							+ "Encrypted sentence: za 91divoc ENICCAV" + System.lineSeparator(),
					outputStream.toString());
		} finally {
			// 還原標準輸入和輸出
			System.setIn(originalSystemIn);
			System.setOut(originalSystemOut);
		}
	}

	@Test
	public void testSentenceEncryption_return_ERROR_multiplePeriod() {

		// 保存原本的標準輸入
		InputStream originalSystemIn = System.in;
		PrintStream originalSystemOut = System.out;

		try {
			// 準備模擬的輸入
			ByteArrayInputStream mockInput = new ByteArrayInputStream("My name M..".getBytes());
			System.setIn(mockInput);

			// 重定向標準輸出
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			System.setOut(new PrintStream(outputStream));

			// 呼叫被測試的方法
			SentenceEncryption.main(null);

			// 斷言標準輸出是否符合預期
			assertEquals("Please enter a sentence to encrypt:" + System.lineSeparator() + "Error. Please re-enter."
					+ System.lineSeparator(), outputStream.toString());
		} finally {
			// 還原標準輸入和輸出
			System.setIn(originalSystemIn);
			System.setOut(originalSystemOut);
		}
	}

}
