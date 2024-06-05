package newtonSquareRoot;

public class NewtonSquareRoot {

	// 牛顿迭代法求平方根
	public static double sqrt(double number) {
		if (number < 0) {
			throw new IllegalArgumentException("Negative numbers have no real square roots");
		}
		if (number == 0) {
			return 0;
		}

		double epsilon = 1e-10; // 精度要求
		double guess = number / 2; // 初始猜测值

		while (Math.abs(guess * guess - number) > epsilon) {
			guess = (guess + number / guess) / 2;
		}

		return guess;
	}

	public static void main(String[] args) {

		double number = Double.parseDouble(args[0]);
		double result = sqrt(number);
		System.out.println("The square root of " + number + " is approximately " + result);
	}

}
