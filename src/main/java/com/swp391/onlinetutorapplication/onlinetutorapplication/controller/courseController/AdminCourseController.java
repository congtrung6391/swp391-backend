package com.swp391.onlinetutorapplication.onlinetutorapplication.controller.courseController;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Course;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.courseRequest.CourseUpdateRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.courseRequest.MaterialCreationRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.MessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.StatusResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse.CourseResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.responseMessage.ErrorMessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.courseService.courseServiceInterface.CourseServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import java.net.http.HttpResponse;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/admin/course")
@CrossOrigin(origins = "localhost:3000/")
public class AdminCourseController {

    @Autowired
    private CourseServiceInterface courseService;

    //Get all course for admin - by Nam
    // localhost:8080/api/admin/course/
    @GetMapping("")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> getAllCourseForAdmin() {
        try {
            return ResponseEntity.ok().body(courseService.getAllCourseInformationForAdmin());
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
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

    //Tạo material - Nam
    @PostMapping(value = "/{courseId}/material", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('TUTOR')")
    public ResponseEntity<?> uploadMaterial(@PathVariable(name = "courseId") Long courseId, MaterialCreationRequest request,@RequestPart(value = "fileAttach",required = false) MultipartFile fileAttach){
        try{
            return ResponseEntity.ok().body(courseService.uploadMaterial(courseId,request,fileAttach));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(e.getMessage()));
        }
    }//  /Course/:courseId/material


    //Edit material - Nam
    @PutMapping(value = "/{courseId}/material/{materialId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAuthority('TUTOR')")
    public ResponseEntity<?> updateMaterial(@PathVariable(name = "courseId") Long courseId,@PathVariable(name = "materialId") Long materialId, MaterialCreationRequest request,@RequestPart("fileAttach") MultipartFile fileAttach){
        try{
            return ResponseEntity.ok().body(courseService.updateMaterial(courseId,materialId,request,fileAttach));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(e.getMessage()));
        }
    }

    //Phần này làm demo thôi, ai có task này thì modify lại - Name
    @GetMapping("/{courseId}/material/{materialId}")
    public ResponseEntity<?> getAllMaterial(@PathVariable(name = "courseId") Long courseId,@PathVariable(name = "materialId") Long materialId){
        try{
            return ResponseEntity.ok().body(courseService.getCourseMaterial(courseId,materialId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(e.getMessage()));
        }
    }

    //Phần này làm demo thôi, ai có task này thì modify lại - Name
    @GetMapping("/{courseId}/material/{materialId}/get-link")
    public ResponseEntity<?> getSharableLink(@PathVariable(name = "courseId")Long courseId,@PathVariable(name = "materialId") String materialId,@RequestParam(name = "fileName") String fileName){
        try{
            return ResponseEntity.ok().body(courseService.getShareableLink(courseId,materialId,fileName));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(e.getMessage()));
        }
    }
}

