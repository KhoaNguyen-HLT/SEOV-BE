package seov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SeovApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeovApplication.class, args);
	}
}
