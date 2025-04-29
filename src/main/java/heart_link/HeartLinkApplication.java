package heart_link;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class HeartLinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(HeartLinkApplication.class, args);
	}

}
