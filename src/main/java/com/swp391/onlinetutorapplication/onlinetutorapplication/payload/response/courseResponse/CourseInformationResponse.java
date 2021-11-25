package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Course;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.CourseStudent;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Subject;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

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
    private Boolean publicStatus;
    private Boolean learningStatus;
    private Boolean registered = false;
    private Subject subject;
    private CourseTutorInformation tutor;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CourseStudentInformation student;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean rejected;
    public CourseInformationResponse(Course course) {
        this.id = course.getId();
        this.courseName = course.getCourseName();
        this.courseDescription = course.getCourseDescription();
        this.grade = course.getGrade();
        this.cost = course.getCost();
        this.length = course.getLength();
        this.subject = course.getSubject();
        this.publicStatus = course.getPublicStatus();
    }

    public CourseInformationResponse() {

    }

    public void setTutor(User tutor) {
        this.tutor = new CourseTutorInformation(tutor);
    }

    public void setStudent(User student) {
        this.student = new CourseStudentInformation(student);
    }
}
