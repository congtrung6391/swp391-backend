package com.swp391.onlinetutorapplication.onlinetutorapplication;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication
public class OnlineTutorApplication {
	public static void main(String[] args) {
		SpringApplication.run(OnlineTutorApplication.class, args);
	}

	@Value("${CUSTOMCONNSTR_CROS_ORIGIN}")
	private String corsOrigin;

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/**")
						// .allowedOrigins(corsOrigin)
						.allowedMethods("GET", "POST", "PUT", "DELETE");
			}
		};
	}
}
