package multiplicationWithoutAddSub;

public class MultiplicationWithoutAddSub {

	public static int multiply(int x, int y) {
		// Initialize result
		int result = 0;

		// Traverse through all bits of y
		while (y > 0) {
			// If the current bit of y is set (i.e., 1)
			if ((y & 1) == 1) {
				result = bitwiseAdd(result, x);
			}

			// Shift x left by 1 (equivalent to multiplying x by 2)
			x <<= 1;

			// Shift y right by 1 (equivalent to dividing y by 2)
			y >>= 1;
		}

		return result;
	}

	// Helper function to perform bitwise addition
	private static int bitwiseAdd(int a, int b) {
		while (b != 0) {
			// Carry now contains common set bits of a and b
			int carry = a & b;

			// Sum of bits of a and b where at least one of the bits is not set
			a = a ^ b;

			// Carry is shifted by one so that adding it to a gives the required sum
			b = carry << 1;
		}
		return a;
	}

	public static void main(String[] args) {

		int x = Integer.parseInt(args[0]);
		int y = Integer.parseInt(args[1]);
		System.out.println("Product of " +args[0] +" and " + args[1] + " answer : " + multiply(x, y));
	}

}
