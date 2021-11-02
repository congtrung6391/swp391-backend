package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.ratingResponse;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Subject;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.rating.Rate;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.userResponse.UserInformationResponse;
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
    private UserInformationResponse student;
    private UserInformationResponse tutor;
    private Subject subject;

    public RatingInformationResponse(Rate rate){
        this.id = rate.getId();
        this.value = rate.getValue();
        this.description = rate.getDescription();
        this.time = rate.getTime();
        this.student = new UserInformationResponse(rate.getStudent());
        this.tutor = new UserInformationResponse(rate.getTutor());
        this.subject = rate.getSubject();
    }
}
