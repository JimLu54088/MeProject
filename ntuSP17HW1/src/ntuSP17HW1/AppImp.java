package ntuSP17HW1;

import java.util.Scanner;

public class AppImp {

    public long retriveScore(int number) {


        // 確保輸入數字在範圍內
        if (number < 0 || number > 9999) {
            System.out.println("輸入的數字不在有效範圍內！");
        }

        //        System.out.println("得分: " + score);

        return calculateScore(number);
    }

    public long calculateScore(int number) {
        int previousResult = -1;
        int currentResult = extractAndSquare(number);
        long score = 1;

        while (currentResult != previousResult) {
            previousResult = currentResult;
            currentResult = extractAndSquare(currentResult);
            score++;

            if (score >= 922337203L) {
                break;
            }

        }

        return score;
    }

    public int extractAndSquare(int number) {
        // 提取百位和十位數字
        int tens = (number / 10) % 10;
        int hundreds = (number / 100) % 10;

        // 組合百位和十位數字
        int twoDigits = hundreds * 10 + tens;

        // 計算平方
        return twoDigits * twoDigits;
    }


}
