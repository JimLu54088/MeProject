package guessNumberQuestionFromWeb;

import java.util.Scanner;

public class GuessNumber {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int low = 0;
		int high = 100;
		boolean guessed = false;

		System.out.println("Please think of a number between 0 and 100!");

		while (!guessed) {
			int guess = (low + high) / 2;
			System.out.println("Is your secret number " + guess + "?");
			System.out.print(
					"Enter 'h' to indicate the guess is too hight. Enter 'l' to indicate the guess is too low. Enter 'c' to indicate I guess correctly.");
			String response = scanner.nextLine();

			if (response.equals("h")) {
				high = guess;
			} else if (response.equals("l")) {
				low = guess;
			} else if (response.equals("c")) {
				guessed = true;
				System.out.println("Game over. Your secret number was: " + guess);
			} else {
				System.out.println("無效輸入，請輸入 'h', 'l', 或 'c'。");
			}
		}

		scanner.close();
	}

}
