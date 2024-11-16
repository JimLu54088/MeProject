package factorFinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FactorFinder {
	public static List<Long> findFactors(long n) {
		List<Long> factors = new ArrayList<>();
		long sqrt = (long) Math.sqrt(n);

		for (long i = 1; i <= sqrt; i++) {
			if (n % i == 0) {
				factors.add(i); // i 是因數
				if (i != n / i) {
					factors.add(n / i); // n / i 也是因數（避免重複添加）
				}
			}
		}

		Collections.sort(factors); // 按順序排列
		return factors;
	}

	public static void main(String[] args) {
//		long n = 9223372036854775806L;
		long n =  Long.parseLong(args[0]);

		long startTime = System.nanoTime();
		List<Long> factors = findFactors(n);
		long endTime = System.nanoTime();
		long executionTime = endTime - startTime;
		System.out.println("Factors of " + n + ": " + factors);
		System.out.println("Execution Time(nanosecond) : " + executionTime);
	}
}