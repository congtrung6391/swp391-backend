package com.swp391.onlinetutorapplication.onlinetutorapplication.controller.courseController;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.CoursePage;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.PublicCourseSearchCriteria;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.MessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse.TimeTableInformation;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse.TimeTableListResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.responseMessage.ErrorMessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.responseMessage.SuccessfulMessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.courseService.courseServiceInterface.CourseServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/public/course")
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
            courseService.handleCourseRegisterByStudent(accessToken, id);
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
                                                   @RequestParam(name = "limit", required = false) Integer limit,
                                                   @RequestParam(name = "courseName", required = false) String courseName,
                                                   @RequestParam(name = "subjectId", required = false) Long subjectId,
                                                   @RequestParam(name = "tutorName", required = false) String fullName,
                                                   @RequestParam(name = "minCost", required = false) Long minCost,
                                                   @RequestParam(name = "maxCost", required = false) Long maxCost,
                                                   @RequestParam(name = "minLength", required = false) Integer minLength,
                                                   @RequestParam(name = "maxLength", required = false) Integer maxLength,
                                                   @RequestHeader(name = "Authorization", required = false) String accessToken) {
        try {
            if (page == null || page < 1) {
                page = 1;
            }
            if (limit == null) {
                limit = 20;
            }
            if (minCost == null) {
                minCost = 0L;
            }
            if (maxCost == null) {
                maxCost = 2000000L;
            }
            if (minLength == null) {
                minLength = 60;
            }
            if (maxLength == null) {
                maxLength = 300;
            }
            CoursePage coursePage = new CoursePage();
            coursePage.setPageSize(limit);
            coursePage.setPageNumber(page - 1);
            PublicCourseSearchCriteria publicCourseSearchCriteria = new PublicCourseSearchCriteria();
            publicCourseSearchCriteria.setCourseName(courseName);
            publicCourseSearchCriteria.setSubjectId(subjectId);
            publicCourseSearchCriteria.setTutorName(fullName);
            publicCourseSearchCriteria.setMinCost(minCost);
            publicCourseSearchCriteria.setMaxCost(maxCost);
            publicCourseSearchCriteria.setMinLength(minLength);
            publicCourseSearchCriteria.setMaxLength(maxLength);
            return ResponseEntity.ok().body(courseService.getAllCourseInformationForStudent(publicCourseSearchCriteria, coursePage, accessToken));
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
    public ResponseEntity<?> getOneCourseApiPublic(@PathVariable(name = "courseId") Long id,
                                                   @RequestHeader(name = "Authorization", required = false) String accessToken) {
        try {
            return ResponseEntity.ok().body(courseService.getOneCourseApiPublic(id, accessToken));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/{courseId}/timetable")
    public ResponseEntity<?> getListTimetable(@PathVariable(name = "courseId") Long courseId) {
        try {
            List<TimeTableInformation> listTimeTable = courseService.getTimeTableList(courseId);
            return ResponseEntity.ok().body(new TimeTableListResponse(listTimeTable));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
        }
    }

    @PutMapping("/{courseId}/reject-course")
    @PreAuthorize("hasAuthority('STUDENT') or hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> studentRejectRegisterCourse(
            @PathVariable(name = "courseId") Long courseId,
            @RequestHeader(name = "Authorization") String accessToken) {
        try {
            courseService.handleStudentRejectRegisterCourse(courseId, accessToken);
            return ResponseEntity.ok().body(new SuccessfulMessageResponse("Reject register course successful"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(e.getMessage()));
        }
    }
}
