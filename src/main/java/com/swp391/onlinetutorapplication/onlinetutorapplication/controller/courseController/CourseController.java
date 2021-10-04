package com.swp391.onlinetutorapplication.onlinetutorapplication.controller.courseController;

import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.courseRequest.CourseCreationRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.MessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.courseService.courseServiceInterface.CourseServiceInterface;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/course")
public class CourseController {
    @Autowired
    private CourseServiceInterface courseService;

    @PostMapping("/tutor/create-course")
    @PreAuthorize("hasAuthority('TUTOR') or hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> createCourse( @RequestHeader(name = "Authorization") String accessToken, @Valid @RequestBody CourseCreationRequest courseCreationRequest) {
        try{
            courseService.handleCourseCreate(courseCreationRequest, accessToken);
            return ResponseEntity.ok().body(new MessageResponse("Create Course done"));
        }catch (NoSuchElementException ex){
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
    }


    @GetMapping("/student/register-course/{id}")
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<?> registerCourse(@RequestHeader(name = "Authorization") String accessToken, @PathVariable(name = "id")Long id){
        try{
            courseService.handleCourseRegister(accessToken, id);
            return ResponseEntity.ok().body(new MessageResponse("Register course successful"));
        }catch (NoSuchElementException ex){
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }catch (Exception ex){
            System.out.println("a");
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
