package sentenceEncryptionDCARDdianzi; //SentenceEncryption

import java.util.Scanner;

public class SentenceEncryption {
	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		System.out.println("Please enter a sentence to encrypt:");

		String input = scanner.nextLine();

		if (!isValidInput(input)) {
			System.out.println("Error. Please re-enter.");
		} else {
			String encryptedSentence = encryptSentence(input);
			System.out.println("Encrypted sentence: " + encryptedSentence);
		}

		scanner.close();

	}

	private static boolean isValidInput(String input) {

		int periodCount = 0;
		// 檢查句子是否只包含英數字空格和句點
		for (char ch : input.toCharArray()) {
			if (!Character.isLetterOrDigit(ch) && ch != ' ' && ch != '.') {
				return false;
			}
			if (ch == '.') {
				periodCount++;
			}
		}
		// 如果句點數量超過一個，返回 false
		return periodCount <= 1;
	}

	private static String encryptSentence(String input) {
		boolean endsWithPeriod = input.endsWith(".");
		// 去掉句尾的句點
		if (endsWithPeriod) {
			input = input.substring(0, input.length() - 1);
		}

		String[] words = input.split(" ");
		StringBuilder encrypted = new StringBuilder();

		for (String word : words) {
			if (encrypted.length() > 0) {
				encrypted.append(" ");
			}
			encrypted.append(encryptWord(word));
		}

		// 在結果末尾加上句點（如果原來句子有句點）
		if (endsWithPeriod) {
			encrypted.append(".");
		}

		return encrypted.toString();
	}

	private static String encryptWord(String word) {
		StringBuilder encryptedWord = new StringBuilder();

		for (char ch : word.toCharArray()) {
			if (Character.isUpperCase(ch)) {
				encryptedWord.append(Character.toLowerCase(ch));
			} else if (Character.isLowerCase(ch)) {
				encryptedWord.append(Character.toUpperCase(ch));
			} else {
				encryptedWord.append(ch);
			}
		}

		return encryptedWord.reverse().toString();
	}
}