package com.swp391.onlinetutorapplication.onlinetutorapplication.controller.courseController;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Course;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.courseRequest.CourseUpdateRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.MessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.StatusResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse.CourseResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.courseService.courseServiceInterface.CourseServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/admin/course")
@CrossOrigin(origins = "localhost:3000/")
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

    // localhost:8080/api/admin/course/id
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN') or hasAuthority('TUTOR')")
    public ResponseEntity<?> deleteCourse(@PathVariable("id") Long id){
        try{
            courseService.deleteCourse(id);
            return ResponseEntity.ok().body(new MessageResponse("Course has been successfully deleted."));
        }catch (NoSuchElementException ex){
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
    }
}

