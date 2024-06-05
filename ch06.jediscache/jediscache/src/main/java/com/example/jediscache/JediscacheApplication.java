package com.example.jediscache;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class JediscacheApplication implements ApplicationRunner {

	private final UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(JediscacheApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		userRepository.save(
				User.builder()
						.name("kj")
						.email("kj@naver.com")
						.build()
		);
		userRepository.save(
				User.builder()
						.name("kj1")
						.email("kj1@naver.com")
						.build()
		);
		userRepository.save(
				User.builder()
						.name("kj2")
						.email("kj2@naver.com")
						.build()
		);
		userRepository.save(
				User.builder()
						.name("kj3")
						.email("kj3@naver.com")
						.build()
		);
	}
}
