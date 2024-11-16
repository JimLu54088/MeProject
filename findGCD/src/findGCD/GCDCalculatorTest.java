package findGCD;

import org.junit.Test;

public class GCDCalculatorTest {

//	@Test
//	public void findTwoNumberGCD() throws Exception {
//
//		String[] args = {"92233720368547752", "922337207845784552" };
//
//		GCDCalculator.main(args);
//
//	}
//
	@Test
	public void findTwoNumberGCD() throws Exception {

		String[] args = { "60", "45" };

		GCDCalculator.main(args);

	}

	@Test
	public void findTwoNumberGCD_if_GCD_is_1() throws Exception {

		String[] args = { "7", "13" };

		GCDCalculator.main(args);

	}

	@Test
	public void input_array_is_blank() throws Exception {

		String[] args = {};

		GCDCalculator.main(args);

	}

	@Test
	public void input_array_is_null() throws Exception {

		GCDCalculator.main(null);

	}


	@Test
	public void initialize_main() throws Exception {

		GCDCalculator gcdCalculator= new  GCDCalculator();

	}

}
