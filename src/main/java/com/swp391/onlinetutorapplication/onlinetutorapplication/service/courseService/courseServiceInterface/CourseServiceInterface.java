package com.swp391.onlinetutorapplication.onlinetutorapplication.service.courseService.courseServiceInterface;

import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.courseRequest.CourseCreationRequest;

public interface CourseServiceInterface {
    void handleCourseCreate(CourseCreationRequest courseCreationRequest);
}
