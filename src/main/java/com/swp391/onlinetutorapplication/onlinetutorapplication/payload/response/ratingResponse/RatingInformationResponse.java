package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.ratingResponse;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.rating.Rate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingInformationResponse {
    private long id;
    private double value;
    private String description;
    private LocalDateTime time;
    private long studentId;
    private long tutorId;
    private long subjectId;

    public RatingInformationResponse(Rate rate){
        this.id = rate.getId();
        this.value = rate.getValue();
        this.description = rate.getDescription();
        this.time = rate.getTime();
        this.studentId = rate.getStudent().getId();
        this.tutorId = rate.getTutor().getId();
        this.subjectId = rate.getSubject().getId();
    }
}
