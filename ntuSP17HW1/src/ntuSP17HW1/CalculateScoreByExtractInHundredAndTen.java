package ntuSP17HW1;

import java.util.Scanner;

public class CalculateScoreByExtractInHundredAndTen {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("請輸入一個0到9999之間的四位數（可以包含0和9999）: ");
        int number = scanner.nextInt();

        // 確保輸入數字在範圍內
        if (number < 0 || number > 9999) {
            System.out.println("輸入的數字不在有效範圍內！");
            return;
        }

        int score = calculateScore(number);
        System.out.println("得分: " + score);
    }

    public static int calculateScore(int number) {
        int previousResult = -1;
        int currentResult = extractAndSquare(number);

        int score = 1;

        while (currentResult != previousResult) {

            System.out.println("currentResult : " + currentResult);

            previousResult = currentResult;
            currentResult = extractAndSquare(currentResult);


            System.out.println("Score : " + score);

            score++;
        }

        return score;
    }

    public static int extractAndSquare(int number) {
        // 提取百位和十位數字
        int tens = (number / 10) % 10;
        int hundreds = (number / 100) % 10;

        // 組合百位和十位數字
        int twoDigits = hundreds * 10 + tens;

        // 計算平方
        return twoDigits * twoDigits;
    }

}
