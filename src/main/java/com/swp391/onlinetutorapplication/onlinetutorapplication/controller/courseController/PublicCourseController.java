package com.swp391.onlinetutorapplication.onlinetutorapplication.controller.courseController;

import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.MessageResponse;

import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse.CourseListResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.responseMessage.ErrorMessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.responseMessage.SuccessfulMessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.courseService.courseServiceInterface.CourseServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/public/course")
@CrossOrigin(origins = "http://localhost:3000/")
public class PublicCourseController {
    @Autowired
    private CourseServiceInterface courseService;

    //Register course- Nam
    // POST   localhost:8080/api/public/course/:id/register
    @PostMapping("/{id}/register")
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<?> registerCourse(@RequestHeader(name = "Authorization") String accessToken, @PathVariable(name = "id") Long id) {
        try {
            courseService.handleCourseRegister(accessToken, id);
            return ResponseEntity.ok().body(new SuccessfulMessageResponse("Register course successful"));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
        }
    }

    //Get course public - by Nam
    // Get localhost:8080/api/public/course
    @GetMapping("")
    public ResponseEntity<?> getAllCourseForPublic() {
        try {
            return ResponseEntity.ok().body(new CourseListResponse(true
                    , courseService.getAllCourseInformationForStudent()));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
        }
    }

    @GetMapping("/subject")
    public ResponseEntity<?> getAllSubjectForPublic() {
        try {
            return ResponseEntity.ok().body(courseService.getSubjectList());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
    }
}
