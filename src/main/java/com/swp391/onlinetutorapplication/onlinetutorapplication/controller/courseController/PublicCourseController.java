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
@CrossOrigin(origins = "https://swp391-onlinetutor.herokuapp.com/")
public class PublicCourseController {
    @Autowired
    private CourseServiceInterface courseService;

    //Register course- Nam
    // POST   localhost:8080/api/public/course/:id/register
    @PostMapping("/{id}/register")
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<?> registerCourse(@RequestHeader(name = "Authorization") String accessToken,
                                            @PathVariable(name = "id") Long id) {
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
    public ResponseEntity<?> getAllCourseForPublic(@RequestParam(name = "page", required = false) Integer page,
                                                   @RequestParam(name = "limit", required = false) Integer limit) {
        try {
            if(page == null){
                page = 1;
            }
            if(limit == null){
                limit = 20;
            }
            return ResponseEntity.ok().body(new CourseListResponse(
                    courseService.getAllCourseInformationForStudent(),
                    page,limit
            ));
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

    @GetMapping("/{courseId}/info")
    public ResponseEntity<?> getOneCourseApiPublic(@PathVariable(name = "courseId") Long id) {
        try {
            return ResponseEntity.ok().body(courseService.getOneCourseApiPublic(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(e.getMessage()));
        }
    }
}
