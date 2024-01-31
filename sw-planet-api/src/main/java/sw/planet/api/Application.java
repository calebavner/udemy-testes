package sw.planet.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sw.planet.api.jacoco.ExcludeFromJacocoGeneratedReport;

@SpringBootApplication
public class Application {
	@ExcludeFromJacocoGeneratedReport
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
