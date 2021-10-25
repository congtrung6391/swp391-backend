package com.swp391.onlinetutorapplication.onlinetutorapplication.service.ratingService.ratingServiceInterface;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.rating.Rate;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.ratingRequest.AddRatingRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.ratingRequest.UpdateRatingRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.ratingResponse.RatingInformationResponse;

import java.util.List;

public interface RatingServiceInterface {
    List<Rate> getAllRating(Long tutorId,Integer page, Integer limit);

    List<Rate> getTutorRatingBySubject(Long tutorId, Long subjectId,Integer page, Integer limit);

    void deleteRating(String accessToken,Long tutorId, Long ratingId);

    Rate addRating(String accessToken, Long tutorId, AddRatingRequest request);

    Rate updateRating(String accessToken, Long tutorId, Long ratingId, UpdateRatingRequest request);
}
