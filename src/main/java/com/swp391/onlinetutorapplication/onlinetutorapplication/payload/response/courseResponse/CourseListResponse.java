package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CourseListResponse {
    private Boolean status = true;
    private Long totalCourse;
    List<CourseInformationResponse> courseList;

    public CourseListResponse(List<CourseInformationResponse> courseList) {
        this.courseList = courseList;
    }

}
