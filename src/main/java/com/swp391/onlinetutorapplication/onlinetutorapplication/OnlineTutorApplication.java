package com.swp391.onlinetutorapplication.onlinetutorapplication;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.ERole;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.Role;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceInterface.UserServiceInterface;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OnlineTutorApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineTutorApplication.class, args);
	}



	/*
	CommandLineRunner run(UserServiceInterface userService){
		return args -> {

			userService.saveRole(new Role(null, ERole.TUTOR));
			userService.saveRole(new Role(null, ERole.STUDENT));
			userService.saveRole(new Role(null, ERole.ADMIN));
			userService.saveRole(new Role(null, ERole.SUPER_ADMIN));

		};
	}
	*/

}
