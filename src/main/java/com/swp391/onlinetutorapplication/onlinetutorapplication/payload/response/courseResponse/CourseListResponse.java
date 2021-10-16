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
    private int totalCourse;
    List<CourseInformationResponse> courseList;

    public CourseListResponse(Boolean status, List<CourseInformationResponse> courseList) {
        this.status = status;
        this.totalCourse = courseList.size();
        this.courseList = courseList;
    }

    public CourseListResponse(List<CourseInformationResponse> courseList, Integer page, Integer limit) {
        page = page-1;
        this.totalCourse = courseList.size();
        List<CourseInformationResponse> responses = new ArrayList<>();
        int index = page*limit;
        int des = courseList.size()<(index+limit) ? courseList.size() : index+limit;
        for(int i = index; i< des ; i++){
            responses.add(courseList.get(i));
        }
        this.courseList = responses;
    }
}
