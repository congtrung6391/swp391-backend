package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.ratingResponse;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.rating.Rate;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.userResponse.UserInformationResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingListResponse {
    private Boolean status = true;
    private Long totalRate;
    private Double avgRate;
    List<RatingInformationResponse> rateList;

    public RatingListResponse(List<RatingInformationResponse> rateList) {
        this.rateList = rateList;
    }

}
