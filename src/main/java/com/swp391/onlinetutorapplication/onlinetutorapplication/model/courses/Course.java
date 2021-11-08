package com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "nvarchar(4000)")
    private String courseName;
    @Column(columnDefinition = "nvarchar(4000)")
    private String courseDescription;
    private int grade;
    private double cost;
    private int length;
    private Boolean learningStatus = false;
    private Boolean publicStatus = false;
    private Boolean status = true;
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private User tutor;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private Set<CourseMaterial> courseMaterial;

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<CourseStudent> courseStudents;
}
