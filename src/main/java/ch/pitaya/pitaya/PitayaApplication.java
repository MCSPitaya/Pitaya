package ch.pitaya.pitaya;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import ch.pitaya.pitaya.model.User;
import ch.pitaya.pitaya.repository.UserRepository;

@SpringBootApplication
public class PitayaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PitayaApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepo, PasswordEncoder bcrypt) {
		return args -> {
			userRepo.save(new User("Test User", "test", "test@test.com", bcrypt.encode("password")));
		};
	}

}
