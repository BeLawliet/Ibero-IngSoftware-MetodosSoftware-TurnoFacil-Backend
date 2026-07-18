package app.turnofacil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import app.turnofacil.config.TurnoFacilProperties;

@SpringBootApplication
@EnableConfigurationProperties(TurnoFacilProperties.class)
public class TurnoFacilApplication {

	public static void main(String[] args) {
		SpringApplication.run(TurnoFacilApplication.class, args);
	}

}
