package ch.pitaya.pitaya;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import ch.pitaya.pitaya.model.Firm;
import ch.pitaya.pitaya.model.User;
import ch.pitaya.pitaya.repository.FirmRepository;
import ch.pitaya.pitaya.repository.UserRepository;

@SpringBootApplication
@EnableScheduling
public class PitayaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PitayaApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepo, FirmRepository firmRepo, PasswordEncoder bcrypt) {
		return args -> {
			// create test user
			if (!userRepo.findByUsernameOrEmail("test", "test").isPresent()) {
				Firm firm = firmRepo.saveAndFlush(new Firm("Pitaya Test Firm"));
				userRepo.saveAndFlush(
						new User("Test User", "test", "test@test.com", bcrypt.encode("password"), firm, "ADMIN"));
			}
			// create tech users
			if (!userRepo.findByUsernameOrEmail("__tech", "__tech").isPresent()) {
				Firm firm = firmRepo.saveAndFlush(new Firm("PITAYA"));
				// user to send off data
				User tech1 = new User("PITAYA", "__tech", "support@example.com", "dummy", firm, "ADMIN");
				tech1.setActive(false);
				tech1.setTechUser(true);
				userRepo.save(tech1);
			}
		};
	}

}
