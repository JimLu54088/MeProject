package ntuSP17HW1;

import java.util.InputMismatchException;
import java.util.Scanner;

public class InputNumberAndTime {

    public static void main(String[] args) {

        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("請輸入一個0到9999之間的四位數（可以包含0和9999）: ");
            int number = scanner.nextInt();

            // 確保輸入數字在範圍內
            if (number < 0 || number > 9999) {
                System.out.println("輸入的數字不在有效範圍內！");
                return;
            }

            System.out.println("Please enter process time : ");
            int times = scanner.nextInt();

            process(number, times);

        } catch (InputMismatchException inputMismatchException) {

            System.out.println("Please enter Integer!!!");

        }


    }

    public static void process(int number, int times) {

        for (int i = 0; i < times; i++) {
            number = extractAndSquare(number);

            if (i == 0) {
                System.out.print(String.format("%04d", number));
            } else {
                System.out.print(" " + String.format("%04d", number));
            }


        }


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
