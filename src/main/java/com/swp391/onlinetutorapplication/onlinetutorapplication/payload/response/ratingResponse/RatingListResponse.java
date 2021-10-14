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
    private Integer totalRate;
    private Double avgRate;
    List<Rate> rateList;

    public RatingListResponse(List<Rate> rateList) {
        this.totalRate = rateList.size();
        this.rateList = rateList;
    }

    public RatingListResponse(List<Rate> rateList, int page, int limit) {
        page = page-1;
        this.totalRate = rateList.size();
        int sum = 0;
        for(Rate rate :rateList){
            sum += rate.getValue();
        }
        this.avgRate = Double.valueOf(sum)/totalRate;
        List<Rate> responses = new ArrayList<>();
        int index = page*limit;
        int des = rateList.size()<(index+limit) ? rateList.size() : index+limit;
        for(int i = index; i< des ; i++){
            responses.add(rateList.get(i));
        }
        this.rateList = responses;
    }
}
