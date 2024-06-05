package multiplicationWithoutAddSub;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

public class TestMultiplicationWithoutAddSub {

	@Test
	public void testMultiplicationWithoutAddSub_case1() {

		String[] args = { "3", "5" };
		PrintStream originalOut = System.out;

		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			System.setOut(new PrintStream(outputStream));

			// �ċ��푪���I���@
			MultiplicationWithoutAddSub.main(args);

			// �Ќ��W���A�o���ە����a��
			assertEquals("Product of 3 and 5 answer : 15" + System.lineSeparator(), outputStream.toString());

		} finally {
			System.setOut(originalOut);
		}

	}

	// This test is to create each class instance so the coverage can be 100%
	@Test
	public void create_instance_test() throws Exception {

		MultiplicationWithoutAddSub multiplicationWithoutAddSub = new MultiplicationWithoutAddSub();

	}

}
