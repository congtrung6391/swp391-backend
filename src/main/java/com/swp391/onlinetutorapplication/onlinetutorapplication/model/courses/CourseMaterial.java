package com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "nvarchar")
    private String description;
    @Column(columnDefinition = "nvarchar")
    private String title;
    private String fileAttach;
    private String linkShare;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    public CourseMaterial(String description, String title, String fileAttach) {
        this.description = description;
        this.title = title;
        this.fileAttach = fileAttach;
    }
}
