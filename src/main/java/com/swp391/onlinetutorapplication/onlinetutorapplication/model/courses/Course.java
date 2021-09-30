package com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String courseName;
    private String courseDescription;
    private int grade;
    private double cost;
    private int length;
    private Boolean courseStatus = false;
    private Boolean status = false;
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private User tutor;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;
}