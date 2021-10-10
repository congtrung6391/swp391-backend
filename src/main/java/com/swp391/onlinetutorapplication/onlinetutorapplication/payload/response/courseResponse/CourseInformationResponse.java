package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Course;
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
    private Subject subject;
    private CourseTutorInformation tutor;
    private CourseStudentInformation student;

    public CourseInformationResponse(Course course) {
        this.id = course.getId();
        this.courseName = course.getCourseName();
        this.courseDescription = course.getCourseDescription();
        this.grade = course.getGrade();
        this.cost = course.getCost();
        this.length = course.getLength();
        this.subject = course.getSubject();
        this.courseStatus = course.getCourseStatus();
    }

    public void setTutor(User tutor) {
        this.tutor = new CourseTutorInformation(tutor);
    }

    public void setStudent(User student) {
        this.student = new CourseStudentInformation(student);
    }
}
