package com.swp391.onlinetutorapplication.onlinetutorapplication.service.ratingService.ratingServiceInterface;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.rating.Rate;

import java.util.List;

public interface RatingServiceInterface {
    List<Rate> getAllRating(Long tutorId);

    List<Rate> getTutorRatingBySubject(Long tutorId, Long subjectId);

    void deleteRating(String accessToken,Long tutorId, Long ratingId);
}
