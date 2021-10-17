package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.ratingResponse;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.rating.Rate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingResponse {
    private RatingInformationResponse rate;
    private boolean status = true;

    public RatingResponse(Rate rate) {
        this.rate = new RatingInformationResponse(rate);
    }
}
