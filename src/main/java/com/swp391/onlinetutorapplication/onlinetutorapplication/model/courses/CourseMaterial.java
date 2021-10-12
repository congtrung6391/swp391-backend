package com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @Column(columnDefinition = "nvarchar(155)")
    private String description;
    @Column(columnDefinition = "nvarchar(155)")
    private String title;
    private String fileAttach;
    private String linkShare;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean status = true;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    public CourseMaterial(String description, String title) {
        this.description = description;
        this.title = title;
    }

    public CourseMaterial(String description, String title, String fileAttach) {
        this.description = description;
        this.title = title;
        this.fileAttach = fileAttach;
    }
}
