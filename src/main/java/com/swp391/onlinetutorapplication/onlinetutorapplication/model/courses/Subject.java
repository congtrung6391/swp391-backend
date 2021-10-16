package com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.rating.Rate;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.ERole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subjectName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "subject")
    private List<Rate> rate;
}
