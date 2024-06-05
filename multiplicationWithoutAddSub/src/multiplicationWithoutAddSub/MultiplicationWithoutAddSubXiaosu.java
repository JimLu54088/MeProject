package multiplicationWithoutAddSub;

public class MultiplicationWithoutAddSubXiaosu {

	public static void main(String[] args) {

		double x = Double.parseDouble(args[0]);
		double y = Double.parseDouble(args[1]);
		System.out.println("Product of " + args[0] + " and " + args[1] + " answer : " + multiply(x, y));

	}

	public static double multiply(double x, double y) {
		// Extract sign, exponent, and mantissa of both numbers
		long bitsX = Double.doubleToRawLongBits(x);
		long bitsY = Double.doubleToRawLongBits(y);

		boolean signX = (bitsX >> 63) != 0;
		boolean signY = (bitsY >> 63) != 0;
		boolean signResult = signX ^ signY;

		int exponentX = (int) ((bitsX >> 52) & 0x7FFL);
		int exponentY = (int) ((bitsY >> 52) & 0x7FFL);
		int exponentResult = exponentX + exponentY - 1023;

		long mantissaX = bitsX & 0xFFFFFFFFFFFFFL;
		long mantissaY = bitsY & 0xFFFFFFFFFFFFFL;

		// Add the implicit leading 1
		mantissaX |= 0x10000000000000L;
		mantissaY |= 0x10000000000000L;

		// Perform multiplication of mantissas
		long mantissaResult = multiplyMantissas(mantissaX, mantissaY);

		// Normalize the result
		if ((mantissaResult & 0x20000000000000L) != 0) {
			mantissaResult >>= 1;
			exponentResult++;
		}

		mantissaResult &= 0xFFFFFFFFFFFFFL;

		// Assemble the result
		long resultBits = ((long) (signResult ? 1 : 0) << 63) | ((long) exponentResult << 52) | mantissaResult;

		return Double.longBitsToDouble(resultBits);
	}

	private static long multiplyMantissas(long x, long y) {
		long result = 0;

		while (y != 0) {
			if ((y & 1) != 0) {
				result = bitwiseAdd(result, x);
			}
			x <<= 1;
			y >>= 1;
		}

		return result;
	}

	private static long bitwiseAdd(long a, long b) {
		while (b != 0) {
			long carry = a & b;
			a = a ^ b;
			b = carry << 1;
		}
		return a;
	}

}
