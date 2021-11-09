package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.CourseStudent;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.userResponse.UserInformationResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseStudentResponse {
    private Long courseStudentId ;
    private UserInformationResponse student;
    private Boolean learningStatus;

    public CourseStudentResponse(CourseStudent courseStudent){
        this.courseStudentId = courseStudent.getId();
        this.student = new UserInformationResponse(courseStudent.getStudent());
        this.learningStatus = courseStudent.getLearningStatus();
    }
}
