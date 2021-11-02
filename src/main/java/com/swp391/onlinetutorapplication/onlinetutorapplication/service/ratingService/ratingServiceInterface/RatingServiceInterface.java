package com.swp391.onlinetutorapplication.onlinetutorapplication.service.ratingService.ratingServiceInterface;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Subject;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.rating.Rate;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.ratingRequest.AddRatingRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.ratingRequest.UpdateRatingRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.ratingResponse.RatingInformationResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.ratingResponse.RatingListResponse;

import java.util.List;

public interface RatingServiceInterface {
    Double getAvgRating(User tutor, Subject subject);

    RatingListResponse getAllRating(Long tutorId, Integer page, Integer limit);

    RatingListResponse getTutorRatingBySubject(Long tutorId, Long subjectId,Integer page, Integer limit);

    void deleteRating(String accessToken,Long tutorId, Long ratingId);

    Rate addRating(String accessToken, Long tutorId, AddRatingRequest request);

    Rate updateRating(String accessToken, Long tutorId, Long ratingId, UpdateRatingRequest request);
}
