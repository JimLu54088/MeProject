package pD1061hw10;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class CompatibilityCalculator {

	public static void main(String[] args) {
//		String inputFolder = "D:\\test_files\\ComplibilityTest"; // 請將此路徑替換為你的inputFolder的實際路徑

		try {

			if (args.length != 1) {
				System.out.println("Please set inputFolder Path.");
				return;
			}

			String inputFolder = args[0];

//			// 取得 inputFolder 中的所有 test.txt 文件
//			Files.list(Paths.get(inputFolder)).filter(path -> path.getFileName().toString().startsWith("test"))
//					.sorted(Comparator.comparing(Path::getFileName)).forEach(path -> processFile(path.toString()));

			try (Stream<Path> paths = Files.list(Paths.get(inputFolder))) {
				Optional<Path> anyPath = paths.filter(path -> path.getFileName().toString().startsWith("test"))
						.findAny();
				if (anyPath.isEmpty()) {
					System.out.println("No matching files found.");
				} else {
					Files.list(Paths.get(inputFolder)).filter(path -> path.getFileName().toString().startsWith("test"))
							.sorted(Comparator.comparing(Path::getFileName))
							.forEach(path -> processFile(path.toString()));

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void processFile(String filePath) {
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			int N = Integer.parseInt(reader.readLine().trim());
			String[] lines = new String[N];

			for (int i = 0; i < N; i++) {
				lines[i] = reader.readLine().trim();
			}

			String searchString = reader.readLine().trim();
			List<Integer> compatibilities = new ArrayList<>();

			for (String line : lines) {
				compatibilities.add(calculateCompatibility(line, searchString));
			}

			// 打印出所有的適配度，使用逗號隔開
			System.out.println(String.join(",", compatibilities.stream().map(String::valueOf).toArray(String[]::new)));

		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException nfe) {
			// Print whole file path
//			System.out.println("The first line of " + filePath + " is not a integer.");

			Path path = Paths.get(filePath);

			// Print file name only
			System.out.println("The first line of " + path.getFileName() + " is not a integer.");

		}
	}

	public static int calculateCompatibility(String testString, String searchString) {
		if (testString.equals(searchString)) {
			return 10000;
		} else if (testString.contains(searchString)) {
			return 1000 + countOccurrences(testString, searchString);
		} else {
			return findLongestCommonSubstring(testString, searchString) * 10;
		}
	}

	public static int countOccurrences(String text, String search) {
		int count = 0;
		Pattern pattern = Pattern.compile("(?=" + Pattern.quote(search) + ")");
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			count++;
		}
		return count;
	}

	public static int findLongestCommonSubstring(String str1, String str2) {
		int[][] lengths = new int[str1.length() + 1][str2.length() + 1];
		int maxLength = 0;

		for (int i = 0; i < str1.length(); i++) {
			for (int j = 0; j < str2.length(); j++) {
				if (str1.charAt(i) == str2.charAt(j)) {
					lengths[i + 1][j + 1] = lengths[i][j] + 1;
					maxLength = Math.max(maxLength, lengths[i + 1][j + 1]);
				}
			}
		}

		return maxLength;
	}
}
