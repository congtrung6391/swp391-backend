package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Subject;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CourseInformationResponse {
    private Long id;
    private String courseName;
    private String courseDescription;
    private int grade;
    private double cost;
    private int length;
    private Boolean courseStatus;
    private String subject;
    private User tutor;
    private String tutorFullName;
    private String tutorPhone;
    private String tutorEmail;
    private User student;
    private Boolean status = true;

    public CourseInformationResponse(Long id,String courseName, String courseDescription, int grade, double cost, int length,String subject, String tutorFullName, String tutorPhone, String tutorEmail) {
        this.id = id;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.grade = grade;
        this.cost = cost;
        this.length = length;
        this.subject = subject;
        this.tutorFullName = tutorFullName;
        this.tutorPhone = tutorPhone;
        this.tutorEmail = tutorEmail;
    }
}
