package com.swp391.onlinetutorapplication.onlinetutorapplication.model.rating;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Subject;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double value;
    private String description;
    private LocalDateTime time;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean status = true;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private User tutor;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    // @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;


    public Rate(Double value, String description, LocalDateTime time, Subject subject) {
        this.value = value;
        this.description = description;
        this.time = time;
        this.subject = subject;
    }
}
