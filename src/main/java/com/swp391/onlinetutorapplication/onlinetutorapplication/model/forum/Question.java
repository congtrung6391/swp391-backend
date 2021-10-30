package com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Subject;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "nvarchar(4000)", nullable = false)
    private String title;
    @Column(columnDefinition = "nvarchar(4000)", nullable = true)
    private String description;
    private LocalDate createdDate = LocalDate.now();
    private Boolean status = true;

    @OneToMany(mappedBy = "question")
    private List<Answer> answer;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = true)
    private Subject subject;


}
