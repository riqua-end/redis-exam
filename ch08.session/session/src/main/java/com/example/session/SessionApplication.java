package com.example.session;

import jakarta.servlet.http.HttpSession;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@SpringBootApplication
@RestController
public class SessionApplication {

	public static void main(String[] args) {
		SpringApplication.run(SessionApplication.class, args);
	}

	@GetMapping("/")
	public Map<String, String> home(HttpSession httpSession) {
		Integer visitCount = (Integer) httpSession.getAttribute("visits");

		if (visitCount == null) {
			visitCount = 0;
		}

		httpSession.setAttribute("visits", ++visitCount);
		return Map.of("session id", httpSession.getId(), "visits", visitCount.toString());
	}

}
