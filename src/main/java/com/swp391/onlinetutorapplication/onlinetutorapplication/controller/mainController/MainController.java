package com.swp391.onlinetutorapplication.onlinetutorapplication.controller.mainController;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class MainController {

    @GetMapping("/auth/all")
    public String accessByAll(){
        return "Public content";
    }

    @GetMapping("/auth/success/student")
    @PreAuthorize("hasAuthority('STUDENT') or hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public String accessStudent(){
        return "Student content";
    }

    @GetMapping("/auth/success/tutor")
    @PreAuthorize("hasAuthority('TUTOR') or hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public String accessTutor(){
        return "Tutor content";
    }

    @GetMapping("/auth/success/admin")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public String accessAdmin(){
        return "Admin content";
    }

    @GetMapping("/auth/success/super-admin")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public String accessSuperAdmin(){
        return "Super Admin content";
    }
}
