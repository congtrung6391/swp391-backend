package com.swp391.onlinetutorapplication.onlinetutorapplication.controller.courseController;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Course;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.courseRequest.ActionApproveOrRejectRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.CourseMaterial;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.courseRequest.CourseCreationRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.courseRequest.CourseUpdateRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.courseRequest.MaterialCreationRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.MessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse.CourseListResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse.CourseResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse.MaterialCreationResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse.MaterialListResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.responseMessage.ErrorMessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.responseMessage.SuccessfulMessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.courseService.courseServiceInterface.CourseServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import javax.validation.Valid;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/admin/course")
@CrossOrigin(origins = "http://localhost:3000/")
public class AdminCourseController {

    @Autowired
    private CourseServiceInterface courseService;

    //Get all course for admin -    by Nam
    // localhost:8080/api/admin/course/
    @GetMapping("")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN') or hasAuthority('TUTOR')")
    public ResponseEntity<?> getAllCourseForAdmin(@RequestHeader(name = "Authorization") String accessToken) {
        try {
            return ResponseEntity.ok().body(new CourseListResponse(true
                    , courseService.getAllCourseInformationForAdmin(accessToken)));
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
            return ResponseEntity.badRequest().body(new SuccessfulMessageResponse("Update Failed"));
        } else {
            return ResponseEntity.ok().body(new CourseResponse(course, "true"));
        }
    }


    //Get one course api - byNam
    // localhost:8080/api/admin/course/:id/info
    @GetMapping("/{courseId}/info")
    public ResponseEntity<?> getOneCourseApi(@RequestHeader(name = "Authorization", required = false) String accessToken, @PathVariable(name = "courseId") Long id) {
        try {
            return ResponseEntity.ok().body(courseService.getOneCourseApi(accessToken, id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(e.getMessage()));
        }
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('TUTOR') or hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> createCourse(@RequestHeader(name = "Authorization") String accessToken, @Valid @RequestBody CourseCreationRequest courseCreationRequest) {
        try {
            Course course = courseService.handleCourseCreate(courseCreationRequest, accessToken);
            return ResponseEntity.ok().body(new CourseResponse(course, "true"));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse("Create failed"));
        }
    }

    // Approve or reject course
    // localhost:8080/api/public/course/:id/register
    @PutMapping("/{id}/register")
    @PreAuthorize("hasAuthority('TUTOR') or hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> handleCourseRegisterRequest(@RequestHeader(name = "Authorization") String accessToken, @PathVariable(name = "id") String id, @RequestBody ActionApproveOrRejectRequest request) {
        try {
            courseService.handleCourseRegisterRequest(accessToken, Long.parseLong(id), request);
            return ResponseEntity.ok().body(new SuccessfulMessageResponse("Course has been processed."));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
        }
    }

    // localhost:8080/api/admin/course/id
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN') or hasAuthority('TUTOR')")
    public ResponseEntity<?> deleteCourse(@PathVariable("id") Long id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.ok().body(new MessageResponse("Course has been successfully deleted."));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
    }

    //Tạo material - Nam
    @PostMapping(value = "/{courseId}/material", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('TUTOR') or hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> uploadMaterial(@PathVariable(name = "courseId") Long courseId,
                                            MaterialCreationRequest request) {
        try {
//            System.out.println(request.getFileAttach());
            return ResponseEntity.ok().body(courseService.uploadMaterial(courseId, request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(e.getMessage()));
        }
    }//  /Course/:courseId/material


    //Edit material - Nam
    @PutMapping(value = "/{courseId}/material/{materialId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAuthority('TUTOR') or hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> updateMaterial(@PathVariable(name = "courseId") Long courseId,
                                            @PathVariable(name = "materialId") Long materialId, MaterialCreationRequest request) {
        try {
            return ResponseEntity.ok().body(courseService.updateMaterial(courseId, materialId, request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(e.getMessage()));
        }
    }

    //Phần này làm demo thôi, ai có task này thì modify lại - Name
    @GetMapping("/{courseId}/material")
    @PreAuthorize("hasAuthority('TUTOR') or hasAuthority('STUDENT') or hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> getAllMaterial(@PathVariable(name = "courseId") Long courseId, @RequestHeader(name = "Authorization") String accessToken) {
        try {
            List<MaterialCreationResponse> materials = courseService.getCourseMaterial(courseId, accessToken);
            return ResponseEntity.ok().body(new MaterialListResponse(materials));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(e.getMessage()));
        }
    }

    //Phần này làm demo thôi, ai có task này thì modify lại - Name
    @GetMapping("/{courseId}/material/{materialId}/get-link")
    public ResponseEntity<?> getSharableLink(@PathVariable(name = "courseId") Long courseId, @PathVariable(name = "materialId") String materialId, @RequestParam(name = "fileName") String fileName) {
        try {
            return ResponseEntity.ok().body(courseService.getShareableLink(courseId, materialId, fileName));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(e.getMessage()));
        }
    }

    @DeleteMapping("{courseId}/material/{materialId}")
    @PreAuthorize("hasAuthority('TUTOR') or hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> deleteMaterial(@PathVariable(name = "courseId") Long courseId, @PathVariable(name = "materialId") Long materialId, @RequestHeader(name = "Authorization") String accessToken) {
        try {
            courseService.deleteMaterial(materialId, courseId, accessToken);
            return ResponseEntity.ok().body(new SuccessfulMessageResponse("Delete Sucess"));

        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
        }
    }
}

