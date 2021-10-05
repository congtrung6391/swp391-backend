package com.swp391.onlinetutorapplication.onlinetutorapplication;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
//
//	CommandLineRunner run(CourseServiceInterface courseServiceInterface){
//		return args -> {
//			courseServiceInterface.saveSubject(new Subject(null,ESubject.VAT_LY));
//			courseServiceInterface.saveSubject(new Subject(null,ESubject.TOAN));
//			courseServiceInterface.saveSubject(new Subject(null,ESubject.HOA_HOC));
//			courseServiceInterface.saveSubject(new Subject(null,ESubject.SINH_HOC));
//		};
//	}
}
