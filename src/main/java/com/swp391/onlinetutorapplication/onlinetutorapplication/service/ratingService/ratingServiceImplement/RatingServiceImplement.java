package com.swp391.onlinetutorapplication.onlinetutorapplication.service.ratingService.ratingServiceImplement;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.rating.Rate;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.rating.RateRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.user.UserRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.ratingService.ratingServiceInterface.RatingServiceInterface;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
@Service
public class RatingServiceImplement implements RatingServiceInterface {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RateRepository rateRepository;

    @Override
    public List<Rate> getAllRating(Long tutorId) {
        User tutor = userRepository.findById(tutorId)
                .orElseThrow(()->{
                    throw new NoSuchElementException("Tutor unauthorizated");
                });
        List<Rate> getListRate = rateRepository.findAllByTutor(tutor);
        return getListRate;
    }
}
