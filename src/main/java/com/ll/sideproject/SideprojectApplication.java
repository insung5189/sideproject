package com.ll.sideproject;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
		System.out.println("현재 실행 환경: " + activeProfile);
		System.out.println("DB_URL: " + System.getProperty("DB_URL"));

		SpringApplication.run(SideprojectApplication.class, args);
	}

	/**
	 * 현재 실행 중인 서버의 IP를 가져오는 메서드
	 */
	private static String getServerIp() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return "UNKNOWN";
		}
	}

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
