package com.swp391.onlinetutorapplication.onlinetutorapplication.controller.courseController;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.AdminCourseSearchCriteria;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Course;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.CoursePage;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.CourseTimetable;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.courseRequest.*;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse.CourseResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse.ListStudentInCourseResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse.MaterialListResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse.TimeTableResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.responseMessage.ErrorMessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.responseMessage.SuccessfulMessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.courseService.courseServiceInterface.CourseServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/admin/course")
public class AdminCourseController {

    @Autowired
    private CourseServiceInterface courseService;

    //Get all course for admin -    by Nam
    // localhost:8080/api/admin/course/
    @GetMapping("")
//    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN') or hasAuthority('TUTOR') or hasAuthority('STUDENT')")
    public ResponseEntity<?> getAllCourseForAdmin(@RequestHeader(name = "Authorization") String accessToken,
                                                  @RequestParam(name = "page", required = false) Integer page,
                                                  @RequestParam(name = "limit", required = false) Integer limit,
                                                  @RequestParam(name = "id", required = false) Long courseId,
                                                  @RequestParam(name = "courseName", required = false) String courseName,
                                                  @RequestParam(name = "subjectId", required = false) Long subjectId,
                                                  @RequestParam(name = "tutorName", required = false) String fullName) {
        try {
            if (page == null || page < 1) {
                page = 1;
            }
            if (limit == null) {
                limit = 20;
            }
            CoursePage coursePage = new CoursePage();
            coursePage.setPageSize(limit);
            coursePage.setPageNumber(page - 1);
            AdminCourseSearchCriteria adminCourseSearchCriteria = new AdminCourseSearchCriteria();
            adminCourseSearchCriteria.setId(courseId);
            adminCourseSearchCriteria.setCourseName(courseName);
            adminCourseSearchCriteria.setSubjectId(subjectId);
            adminCourseSearchCriteria.setTutorName(fullName);

            return ResponseEntity.ok().body(courseService.getAllCourseInformationForAdmin(accessToken,
                    adminCourseSearchCriteria,
                    coursePage));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
        }
    }

    // localhost:8080/api/admin/course/:id
    @PutMapping("/{courseId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN') or hasAuthority('TUTOR')")
    public ResponseEntity<?> updateCourse(@RequestBody CourseUpdateRequest request,
                                          @RequestHeader(name = "Authorization") String accessToken,
                                          @PathVariable(name = "courseId") String id) {
        Course course = courseService.updateCourse(request, Long.parseLong(id), accessToken);
        if (course == null) {
            return ResponseEntity.badRequest().body(new SuccessfulMessageResponse("Update Failed"));
        } else {
            return ResponseEntity.ok().body(new CourseResponse(course));
        }
    }


    //Get one course api - byNam
    // localhost:8080/api/admin/course/:id/info
    @GetMapping("/{courseId}/info")
    public ResponseEntity<?> getOneCourseApi(@RequestHeader(name = "Authorization", required = false) String accessToken,
                                             @PathVariable(name = "courseId") Long id) {
        try {
            return ResponseEntity.ok().body(courseService.getOneCourseApiAdmin(accessToken, id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(e.getMessage()));
        }
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('TUTOR') or hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> createCourse(@RequestHeader(name = "Authorization") String accessToken,
                                          @Valid @RequestBody CourseCreationRequest courseCreationRequest) {
        try {
            Course course = courseService.handleCourseCreate(courseCreationRequest, accessToken);
            return ResponseEntity.ok().body(new CourseResponse(course));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse("Create failed"));
        }
    }

    // Approve or reject course
    // localhost:8080/api/admin/course/:id/student/:courseStudentId
    @PutMapping("/{id}/student/{courseStudentId}")
    @PreAuthorize("hasAuthority('TUTOR') or hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> handleCourseRegisterRequest(@RequestHeader(name = "Authorization") String accessToken,
                                                         @PathVariable(name = "id") String id,
                                                         @PathVariable(name = "courseStudentId") Long courseStudentId,
                                                         @RequestBody ActionApproveOrRejectRequest request) {
        try {
            courseService.handleCourseRegisterByTutor(accessToken, Long.parseLong(id), courseStudentId, request);
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
    public ResponseEntity<?> deleteCourse(@PathVariable("id") String id) {
        try {
            courseService.deleteCourse(Long.parseLong(id));
            return ResponseEntity.ok().body(new SuccessfulMessageResponse("Course has been successfully deleted."));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse("Course not found"));
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
                                            @PathVariable(name = "materialId") Long materialId,
                                            MaterialCreationRequest request) {
        try {
            return ResponseEntity.ok().body(courseService.updateMaterial(courseId, materialId, request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(e.getMessage()));
        }
    }

    //Phần này làm demo thôi, ai có task này thì modify lại - Name
    @GetMapping("/{courseId}/material")
    @PreAuthorize("hasAuthority('TUTOR') or hasAuthority('STUDENT') or hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> getAllMaterial(@PathVariable(name = "courseId") Long courseId,
                                            @RequestHeader(name = "Authorization") String accessToken,
                                            @RequestParam(name = "page", required = false) Integer page,
                                            @RequestParam(name = "limit", required = false) Integer limit) {
        try {
            if (page == null || page < 1) {
                page = 1;
            }
            if (limit == null) {
                limit = 20;
            }
            MaterialListResponse materials = courseService.getCourseMaterial(courseId, accessToken, page, limit);
            return ResponseEntity.ok().body(materials);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(e.getMessage()));
        }
    }

    //Phần này làm demo thôi, ai có task này thì modify lại - Name
    @GetMapping("/{courseId}/material/{materialId}/get-link")
    public ResponseEntity<?> getSharableLink(@PathVariable(name = "courseId") Long courseId,
                                             @PathVariable(name = "materialId") String materialId,
                                             @RequestParam(name = "fileName") String fileName) {
        try {
            return ResponseEntity.ok().body(courseService.getShareableLink(courseId, materialId, fileName));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(e.getMessage()));
        }
    }

    @DeleteMapping("{courseId}/material/{materialId}")
    @PreAuthorize("hasAuthority('TUTOR') or hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> deleteMaterial(@PathVariable(name = "courseId") Long courseId,
                                            @PathVariable(name = "materialId") Long materialId,
                                            @RequestHeader(name = "Authorization") String accessToken) {
        try {
            courseService.deleteMaterial(materialId, courseId, accessToken);
            return ResponseEntity.ok().body(new SuccessfulMessageResponse("Delete Sucess"));

        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
        }
    }

    @DeleteMapping("{courseId}/timetable/{timetableId}")
    @PreAuthorize("hasAuthority('TUTOR') or  hasAuthority('SUPER_ADMIN') or hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteTimeTable(@PathVariable(name = "courseId") Long courseId,
                                             @PathVariable(name = "timetableId") Long timetableId,
                                             @RequestHeader(name = "Authorization") String accessToken) {
        try {
            courseService.deleteTimeTable(timetableId, courseId, accessToken);
            return ResponseEntity.ok().body(new SuccessfulMessageResponse("Delete Success"));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
        }
    }

    @PutMapping("/{courseId}/timetable/{timetableId}")
    @PreAuthorize("hasAuthority('TUTOR') or hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> updateTimeTable(@PathVariable(name = "courseId") Long courseId,
                                             @PathVariable(name = "timetableId") Long timetableId,
                                             @RequestBody TimeTableRequest request) {
        try {
            CourseTimetable timetable = courseService.updateCourseTimeTable(timetableId, courseId, request);
            return ResponseEntity.ok().body(new TimeTableResponse(timetable));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
        }
    }

    @PostMapping("/{courseId}/timetable")
    @PreAuthorize("hasAuthority('TUTOR') or hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> createTimetable(@RequestHeader(name = "Authorization") String accessToken, @PathVariable(name = "courseId") Long courseId, @Valid @RequestBody TimeTableCreationRequest request) {
        try {
            CourseTimetable timetable = courseService.createTimetable(request, courseId, accessToken);
            return ResponseEntity.ok().body(new TimeTableResponse(timetable));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
        }
    }

    @PutMapping("/{courseId}/toggle-public")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> handleToggleCourseByAdmin(@PathVariable(name = "courseId") Long courseId) {
        try {
            courseService.handleToggleCourseByAdmin(courseId);
            return ResponseEntity.ok().body(new SuccessfulMessageResponse("Toggle course public successful"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/{courseId}/student")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN') or hasAuthority('TUTOR')")
    public ResponseEntity<?> getListStudentInCourse(@PathVariable(name = "courseId") Long courseId,
                                                    @RequestHeader(name = "Authorization") String accessToken,
                                                    @RequestParam(required = false) String studentId,
                                                    @RequestParam(required = false) String studentName,
                                                    @RequestParam(required = false) Integer page,
                                                    @RequestParam(required = false) Integer limit) {
        try {
            if (page == null || page < 1) {
                page = 1;
            }
            if (limit == null) {
                limit = 20;
            }
            ListStudentInCourseResponse response = courseService.getListStudentInOneCourse(courseId,accessToken, studentId, studentName, page, limit);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(e.getMessage()));
        }
    }
}

