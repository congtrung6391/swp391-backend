package com.swp391.onlinetutorapplication.onlinetutorapplication.model.rating;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private Integer value;

    private String description;
    private LocalDateTime time;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private User tutor;

    public Rate(Integer value, String description, LocalDateTime time) {
        this.value = value;
        this.description = description;
        this.time = time;
    }
}
