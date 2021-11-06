package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ListStudentInCourseResponse {
    private Boolean status = true;
    private Long totalStudent;
    private List<CourseStudentResponse> listStudent;

    public ListStudentInCourseResponse(List<CourseStudentResponse> listStudent){
        this.listStudent = listStudent;
    }
}
