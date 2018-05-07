package ch.pitaya.pitaya;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import ch.pitaya.pitaya.model.Court;
import ch.pitaya.pitaya.model.Firm;
import ch.pitaya.pitaya.model.User;
import ch.pitaya.pitaya.repository.CourtRepository;
import ch.pitaya.pitaya.repository.FirmRepository;
import ch.pitaya.pitaya.repository.UserRepository;

@SpringBootApplication
@EnableScheduling
public class PitayaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PitayaApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepo, CourtRepository courtRepo, FirmRepository firmRepo, PasswordEncoder bcrypt) {
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
			// create a few court records to work with
			if (courtRepo.findAll().isEmpty()) {
				Court court1 = new Court("Kanzlei Schlichtungsbehörde", "Bäumleingasse", "5", "Basel", "4051", "+41 61 267 64 39", null);
				Court court2 = new Court("Schlichtungsbehörde Bern-Mittelland", "Effingerstrasse", "34", "Bern", "3008", "+41 31 635 47 50", null);
				courtRepo.save(court1);
				courtRepo.save(court2);
			}
		};
	}

}
