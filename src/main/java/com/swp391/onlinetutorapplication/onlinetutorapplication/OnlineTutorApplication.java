package com.swp391.onlinetutorapplication.onlinetutorapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OnlineTutorApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineTutorApplication.class, args);
	}


//	CommandLineRunner run(UserServiceInterface userService){
//		return args -> {
//
////			userService.saveRole(new Role(null,"TUTOR"));
////			userService.saveRole(new Role(null,"STUDENT"));
////			userService.saveRole(new Role(null,"ADMIN"));
////			userService.saveRole(new Role(null,"SUPER_ADMIN"));
//
//		};
//	}
}
