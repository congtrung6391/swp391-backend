package com.swp391.onlinetutorapplication.onlinetutorapplication.service.courseService.courseServiceInterface;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Course;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Subject;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.courseRequest.CourseCreationRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse.CourseInformationResponse;

import java.util.List;

public interface CourseServiceInterface {
    void handleCourseCreate(CourseCreationRequest courseCreationRequest);
    List<CourseInformationResponse> getAllCourseInformationForAdmin();
    List<CourseInformationResponse> getAllCourseInformationForStudent();
    void handleCourseRegister(String accessToken, Long id);
    void saveSubject(Subject subject);
}