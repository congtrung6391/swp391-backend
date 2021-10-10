package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CourseListResponse {
    private String status;
    private int totalCourse;
    List<CourseInformationResponse> courses;

    public CourseListResponse(String status, List<CourseInformationResponse> courses) {
        this.status = status;
        this.totalCourse = courses.size();
        this.courses = courses;
    }
}
