package jp.co.jim.mainTest;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GetLocalIP {
    public static void main(String[] args) {
        try {
            // 獲取本機 IP 地址
            InetAddress inetAddress = InetAddress.getLocalHost();
            // 輸出 IPv4 地址
            System.out.println("IPv4 Address: " + inetAddress.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}

