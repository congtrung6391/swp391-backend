package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CourseListResponse {
    private Boolean status = true;
    private int totalCourse;
    List<CourseInformationResponse> courseList;

    public CourseListResponse(Boolean status, List<CourseInformationResponse> courseList) {
        this.status = status;
        this.totalCourse = courseList.size();
        this.courseList = courseList;
    }
}
