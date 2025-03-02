package com.ll.sideproject;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = "spring.profiles.active=test")
class SideprojectApplicationTests {

	@BeforeAll
	static void setup() {
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

		// 테스트 환경에서는 강제로 "test" 프로파일 설정
		System.setProperty("spring.profiles.active", "test");

		// 실행 환경에 맞게 데이터베이스 환경 변수 적용
		if ("prod".equals(activeProfile)) {
			setPropertyDbUrl = dotenv.get("PROD_TEST_DB_URL");
			setPropertyDbUsername = dotenv.get("PROD_TEST_DB_USERNAME");
			setPropertyDbPassword = dotenv.get("PROD_TEST_DB_PASSWORD");
		} else {
			setPropertyDbUrl = dotenv.get("LOCAL_TEST_DB_URL");
			setPropertyDbUsername = dotenv.get("LOCAL_TEST_DB_USERNAME");
			setPropertyDbPassword = dotenv.get("LOCAL_TEST_DB_PASSWORD");
		}

		// 시스템 프로퍼티 적용
		System.setProperty("DB_URL", setPropertyDbUrl);
		System.setProperty("DB_USERNAME", setPropertyDbUsername);
		System.setProperty("DB_PASSWORD", setPropertyDbPassword);

		System.out.println("현재 실행 환경: " + activeProfile);
		System.out.println("DB_URL: " + System.getProperty("DB_URL"));
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

	@Test
	void contextLoads() {
		assertNotNull(System.getProperty("DB_URL"), "DB_URL should not be null");
		assertNotNull(System.getProperty("DB_USERNAME"), "DB_USERNAME should not be null");
		assertNotNull(System.getProperty("DB_PASSWORD"), "DB_PASSWORD should not be null");
	}
}