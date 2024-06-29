package jp.co.jim;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class DemoApplication {

	@Value("${localDirectory}")
	private String localDirectory;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

//停止springboot
//		try {
//			// 指定批处理文件的路径
//			String batFilePath = "D:\\test_files\\20240629Warexcecute\\endJob.bat";
//			// 执行批处理文件
//			Process process = Runtime.getRuntime().exec(batFilePath);
//			// 等待批处理文件执行完毕
//			process.waitFor();
//		} catch (IOException | InterruptedException e) {
//			e.printStackTrace();
//		}

	}

	@PostConstruct
	public void printLocalDirectory() {
		System.out.println("Local Directory: " + localDirectory);

	}

}
