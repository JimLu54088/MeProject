package jp.co.jim.util;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class IpAddressPrinter implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            System.out.println("\nMain Page URL: " + "http://" + ip.getHostAddress() + ":8080/home.html\n");
        } catch (UnknownHostException e) {
            System.err.println("Unable to determine the IP address.");
            e.printStackTrace();
        }
    }
}
