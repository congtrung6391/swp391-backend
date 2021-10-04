package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.courseRequest;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Subject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CourseCreationRequest {
    private String courseName;
    private String courseDescription;
    private int grade;
    private double cost;
    private int length;
    private String subject;
}
