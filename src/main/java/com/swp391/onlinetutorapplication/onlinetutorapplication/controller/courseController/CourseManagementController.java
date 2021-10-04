package com.swp391.onlinetutorapplication.onlinetutorapplication.controller.courseController;

import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.MessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.courseService.courseServiceInterface.CourseServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/course")
@CrossOrigin(origins = "localhost:3000/")
public class CourseManagementController {

    @Autowired
    private CourseServiceInterface courseService;

    @GetMapping("/all-courses")
    public ResponseEntity<?> getAllCourseForStudent(){
        try {
            return ResponseEntity.ok().body(courseService.getAllCourseInformationForStudent());
        }catch (NoSuchElementException ex){
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
    }



    @GetMapping("/admin/manage/all-courses")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> getAllCourseForAdmin(){
        try {
            return ResponseEntity.ok().body(courseService.getAllCourseInformationForAdmin());
        }catch (NoSuchElementException ex){
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
    }

    @DeleteMapping("/tutor/delete/{id}")
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
