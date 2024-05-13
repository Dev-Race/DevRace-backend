package com.sajang.devracebackend;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.TimeZone;

// @EnableJpaAuditing
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})  // 데이터베이스 사용시 해제할것.
public class DevraceBackendApplication {

	@PostConstruct
	public void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}

	public static void main(String[] args) {
		SpringApplication.run(DevraceBackendApplication.class, args);
	}

}
