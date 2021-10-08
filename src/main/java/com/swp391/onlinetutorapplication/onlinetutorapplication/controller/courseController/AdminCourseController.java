package com.swp391.onlinetutorapplication.onlinetutorapplication.controller.courseController;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Course;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.courseRequest.CourseCreationRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.courseRequest.CourseUpdateRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.MessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.StatusResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse.CourseResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.courseService.courseServiceInterface.CourseServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/admin/course")
@CrossOrigin(origins = "http://localhost:3000/")
public class AdminCourseController {

    @Autowired
    private CourseServiceInterface courseService;

    // localhost:8080/api/admin/course/all-courses
    @GetMapping("")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> getAllCourseForAdmin() {
        try {
            return ResponseEntity.ok().body(courseService.getAllCourseInformationForAdmin());
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
    }

    // localhost:8080/api/admin/course/:id
    @PutMapping("/{courseId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN') or hasAuthority('TUTOR')")
    public ResponseEntity<?> updateCourse(@RequestBody CourseUpdateRequest request, @RequestHeader(name = "Authorization") String accessToken, @PathVariable(name = "courseId") String id) {
        Course course = courseService.updateCourse(request, Long.parseLong(id), accessToken);
        if (course == null) {
            return ResponseEntity.badRequest().body(new StatusResponse("Update Failed", "false"));
        } else {
            return ResponseEntity.ok().body(new CourseResponse(course, "true"));
        }
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('TUTOR') or hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> createCourse( @RequestHeader(name = "Authorization") String accessToken, @Valid @RequestBody CourseCreationRequest courseCreationRequest) {
        try{
            Course course= courseService.handleCourseCreate(courseCreationRequest, accessToken);
            return ResponseEntity.ok().body(new CourseResponse(course,"true"));
        }catch (NoSuchElementException ex){
            return ResponseEntity.badRequest().body(new StatusResponse("Create failed","false"));
        }
    }
}

