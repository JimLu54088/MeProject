package encryptdecrypt;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

public class EncryptDecryptTest {

	@Test
	public void encrypt_test() throws Exception {

		// �ۑ����{�I�W���A�o
		PrintStream originalOut = System.out;

		try {
			// �n����� ByteArray �Ґڝ��A�o
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			System.setOut(new PrintStream(outputStream));

			// �ċ��푪���I���@
			Encrypt.encrypt("tttuuui85885");

			// �Ќ��W���A�o���ە����a��
			assertEquals(
					"Original Message : tttuuui85885" + System.lineSeparator()
							+ "Encrypted Message : xccktX6lnlC0BRUgcSYPew==" + System.lineSeparator(),
					outputStream.toString());

		} finally {
			// �Ҍ��W���A�o
			System.setOut(originalOut);
		}

	}

	@Test
	public void decrypt_test() throws Exception {

		// �ۑ����{�I�W���A�o
		PrintStream originalOut = System.out;

		try {
			// �n����� ByteArray �Ґڝ��A�o
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			System.setOut(new PrintStream(outputStream));

			// �ċ��푪���I���@
			Decrypt.decrypt("xccktX6lnlC0BRUgcSYPew==");

			// �Ќ��W���A�o���ە����a��
			assertEquals("Encrypted Message : xccktX6lnlC0BRUgcSYPew==" + System.lineSeparator()
					+ "Decrypted Message : tttuuui85885" + System.lineSeparator(), outputStream.toString());

		} finally {
			// �Ҍ��W���A�o
			System.setOut(originalOut);
		}

	}

	@Test
	public void main_test_en() throws Exception {

		String[] args = { "Encrypt", "tttuuui85885" };
		// �ۑ����{�I�W���A�o
		PrintStream originalOut = System.out;

		try {
			// �n����� ByteArray �Ґڝ��A�o
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			System.setOut(new PrintStream(outputStream));

			// �ċ��푪���I���@
			EncryptDecryptExecutor.main(args);

			// �Ќ��W���A�o���ە����a��
			assertEquals(
					"Original Message : tttuuui85885" + System.lineSeparator()
							+ "Encrypted Message : xccktX6lnlC0BRUgcSYPew==" + System.lineSeparator(),
					outputStream.toString());

		} finally {
			// �Ҍ��W���A�o
			System.setOut(originalOut);
		}

	}

	@Test
	public void main_test_de() throws Exception {

		String[] args = { "Decrypt", "xccktX6lnlC0BRUgcSYPew==" };
		// �ۑ����{�I�W���A�o
		PrintStream originalOut = System.out;

		try {
			// �n����� ByteArray �Ґڝ��A�o
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			System.setOut(new PrintStream(outputStream));

			// �ċ��푪���I���@
			EncryptDecryptExecutor.main(args);

			// �Ќ��W���A�o���ە����a��
			assertEquals("Encrypted Message : xccktX6lnlC0BRUgcSYPew==" + System.lineSeparator()
					+ "Decrypted Message : tttuuui85885" + System.lineSeparator(), outputStream.toString());

		} finally {
			// �Ҍ��W���A�o
			System.setOut(originalOut);
		}

	}

	@Test
	public void main_test_doNothing() throws Exception {
		String[] args = { "Test", "xccktX6lnlC0BRUgcSYPew==" };

		// �ċ��푪���I���@
		EncryptDecryptExecutor.main(args);

	}

	@Test
	public void enctypt_fail() throws Exception {

		// �ċ��푪���I���@
		Encrypt.encrypt(null);

	}

	@Test
	public void dectypt_fail() throws Exception {

		// �ċ��푪���I���@
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
