package com.tlvlp.iot.server.portal;

import com.tlvlp.iot.server.portal.config.SecretsLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

import java.util.List;


@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class Application {

	public static void main(String[] args) {
		setSpringProfileArgumentInSecretsLoader(args);
		SpringApplication.run(Application.class, args);
	}

	private static void setSpringProfileArgumentInSecretsLoader(String[] args) {
		List.of(args)
				.stream()
				.filter(arg -> arg.toLowerCase().contains("spring.profiles.active"))
				.findFirst()
				.ifPresent(profileArg ->
						SecretsLoader.springProfileArg = profileArg.split("=", 0)[1]);
	}

}
