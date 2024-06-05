package newtonSquareRoot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

public class NewtonSquareRootTest {

	@Test
	public void testNewtonSquareRoot_case1() {

		String[] args = { "100" };
		PrintStream originalOut = System.out;

		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			System.setOut(new PrintStream(outputStream));

			// 呼叫被測試的方法
			NewtonSquareRoot.main(args);

			// 斷言標準輸出是否符合預期
			assertEquals("The square root of 100.0 is approximately 10.0" + System.lineSeparator(),
					outputStream.toString());

		} finally {
			System.setOut(originalOut);
		}

	}

	@Test
	public void testNewtonSquareRoot_case2_input_0() {

		String[] args = { "0" };
		PrintStream originalOut = System.out;

		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			System.setOut(new PrintStream(outputStream));

			// 呼叫被測試的方法
			NewtonSquareRoot.main(args);

			// 斷言標準輸出是否符合預期
			assertEquals("The square root of 0.0 is approximately 0.0" + System.lineSeparator(),
					outputStream.toString());

		} finally {
			System.setOut(originalOut);
		}

	}

	// This test is to create each class instance so the coverage can be 100%
	@Test
	public void create_instance_test() throws Exception {

		NewtonSquareRoot newtonSquareRoot = new NewtonSquareRoot();

	}

	@Test
	public void testNegativeNumber() {
		try {

			String[] args = { "-1" };
			NewtonSquareRoot.main(args);
		} catch (IllegalArgumentException e) {
			assertEquals("Negative numbers have no real square roots", e.getMessage());
		}
	}

}
