package com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CourseStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;
    private Boolean learningStatus = false;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean status = true;

    public CourseStudent(Course course, User student){
        this.course = course;
        this.student = student;
    }
}
