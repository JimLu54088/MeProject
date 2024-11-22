package guiwuzhanliFromDcard;

import java.util.Scanner;

public class GuiwuZanli {

	 public static void main(String[] args) {
	        Scanner scanner = new Scanner(System.in);

	        // 讀取第一行
	        String[] firstLine = scanner.nextLine().split(" ");
	        int M = Integer.parseInt(firstLine[0]); // 怪物數量
	        int A = Integer.parseInt(firstLine[1]); // 玩家初始戰力

	        // 讀取第二行 (怪物戰力值)
	        int[] monsterPower = new int[M];
	        String[] secondLine = scanner.nextLine().split(" ");
	        for (int i = 0; i < M; i++) {
	            monsterPower[i] = Integer.parseInt(secondLine[i]);
	        }

	        // 模擬擊敗怪物的過程
	        int playerPower = A;
	        for (int i = 0; i < M; i++) {
	            if (playerPower > monsterPower[i]) {
	                playerPower += monsterPower[i]; // 玩家擊敗怪物並吸收戰力
	            } else {
	                break; // 玩家無法擊敗怪物，遊戲結束
	            }
	        }

	        // 輸出結果
	        System.out.println(playerPower);

	        scanner.close();
	    }


}
