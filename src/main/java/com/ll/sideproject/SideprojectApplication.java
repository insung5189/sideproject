package com.ll.sideproject;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@SpringBootApplication
public class SideprojectApplication {

	public static void main(String[] args) {
		// .env 파일 로드
		Dotenv dotenv = Dotenv.load();

		// activeProfile 변수 선언
		String activeProfile;

		// 데이터베이스 환경변수 초기화
		String setPropertyDbUrl = null;
		String setPropertyDbUsername = null;
		String setPropertyDbPassword = null;

		// 현재 실행 중인 서버의 IP 주소 가져오기
		String currentIp = getServerIp();

		// IP 주소를 기반으로 실행 환경 결정(공인ip의 일부만 비교)
		activeProfile = currentIp.startsWith("211.37.") ? "prod" : "dev";

		// 실행 환경을 시스템 속성으로 설정
		System.setProperty("spring.profiles.active", activeProfile);

		// 실행 환경에 맞게 데이터베이스 환경 변수 적용
		if ("prod".equals(activeProfile)) {
			setPropertyDbUrl = dotenv.get("PROD_DB_URL");
			setPropertyDbUsername = dotenv.get("PROD_DB_USERNAME");
			setPropertyDbPassword = dotenv.get("PROD_DB_PASSWORD");
		} else {
			setPropertyDbUrl = dotenv.get("LOCAL_DB_URL");
			setPropertyDbUsername = dotenv.get("LOCAL_DB_USERNAME");
			setPropertyDbPassword = dotenv.get("LOCAL_DB_PASSWORD");
		}

		// 시스템 프로퍼티 적용
		System.setProperty("DB_URL", setPropertyDbUrl);
		System.setProperty("DB_USERNAME", setPropertyDbUsername);
		System.setProperty("DB_PASSWORD", setPropertyDbPassword);

		System.out.println("현재 서버 IP: " + maskIp(currentIp)); // IP 일부만 노출
		System.out.println("현재 서버 IP: " + currentIp); // IP 일부만 노출
		System.out.println("현재 실행 환경: " + activeProfile);
		System.out.println("DB_URL: " + System.getProperty("DB_URL"));

		SpringApplication.run(SideprojectApplication.class, args);
	}

	/**
	 * 현재 실행 중인 서버의 IP를 가져오는 메서드
	 */
	/**
	 * 현재 실행 중인 서버의 IP를 가져오는 메서드
	 */
	private static String getServerIp() {
		try {
			URL url = new URL("https://checkip.amazonaws.com");
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String ip = reader.readLine();
			reader.close();
			return ip;
		} catch (Exception e) {
			e.printStackTrace();
			return "UNKNOWN";
		}
	}
//	private static String getServerIp() {
//		try {
//			URL url = new URL("https://ifconfig.me");
//			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//			connection.setRequestMethod("GET");
//
//			// User-Agent 추가
//			connection.setRequestProperty("User-Agent", "Mozilla/5.0");
//
//			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//			String ip = reader.readLine();
//			reader.close();
//
//			return ip;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "UNKNOWN";
//		}
//	}

//	private static String getServerIp() {
//		try {
//			// 1. 컨테이너 내부가 아니라 외부(공인) IP를 가져옴
//			Process process = Runtime.getRuntime().exec("curl -s ifconfig.me");
//			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//			String externalIp = reader.readLine();
//			process.waitFor();
//			return externalIp;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "UNKNOWN";
//		}
//	}


	/**
	 * IP 주소의 일부만 표시하여 노출을 방지하는 메서드 (예: 211.37.xxx.xxx)
	 */
	private static String maskIp(String ip) {
		if (ip == null || ip.equals("UNKNOWN")) {
			return "UNKNOWN";
		}
		String[] parts = ip.split("\\.");
		if (parts.length == 4) {
			return parts[0] + "." + parts[1] + ".xxx.xxx"; // 앞 2자리만 표시
		}
		return ip;
	}
}
