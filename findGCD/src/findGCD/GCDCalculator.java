package findGCD;

import java.util.Arrays;

public class GCDCalculator {
	// 計算兩個數的 GCD
	private static long gcd(long a, long b) {
		while (b != 0) {
			long temp = b;
			b = a % b;
			a = temp;
		}
		return a;
	}

	// 計算數組的 GCD
	public static long findGCD(long[] nums) {

		long result = nums[0];

		// 遍歷數組，逐步計算 GCD
		for (int i = 1; i < nums.length; i++) {
			result = gcd(result, nums[i]);

			// 如果 GCD 已經是 1，可以提前結束
			if (result == 1) {
				return 1;
			}
		}

		return result;
	}

	public static void main(String[] args) {
//        long[] nums = {92233720368547752L, 922337207845784552L, 7485789999682L, 74857845789824L, 7487654399682L, 7485789889682L, 7485799944478L, 7485789914578L, 7485789947858L, 748578994447774L, 7485789912354L, 7485789999748892L, 7485789999478L, 748578997745882L, 922337207845784552L, 922337207845784552L, 922337207845784552L, 922337207845784552L, 922337207845784552L, 922337207845784552L, 922337207845784552L, 922337207845784552L, 922337207845784552L, 922337207845784552L, 922337207845784552L, 922337207845784552L, 922337207845784552L, 922337207845784552L, 922337207845784552L, 922337207845784552L, 922337207845784552L, 922337207845784552L, 922337207845784552L, 922337207845784552L, 922337207845784552L, 922337207845784552L, 922337207845784552L};
//        long[] nums = {};

		if (args == null || args.length == 0) {
			System.err.println("數組不能為空");
			return;
		}

		long[] nums = Arrays.stream(args).mapToLong(Long::parseLong).toArray();

		long startTime = System.nanoTime();
		long result_GCD = findGCD(nums);
		long endTime = System.nanoTime();
		long executionTime = endTime - startTime;
		System.out.println("最大公因數為: " + result_GCD);
		System.out.println("Execution Time(nanosecond) : " + executionTime);

	}

}
