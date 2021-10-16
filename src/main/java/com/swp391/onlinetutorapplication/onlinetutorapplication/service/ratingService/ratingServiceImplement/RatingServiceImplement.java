package com.swp391.onlinetutorapplication.onlinetutorapplication.service.ratingService.ratingServiceImplement;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Subject;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.rating.Rate;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.ERole;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.Role;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.course.SubjectRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.rating.RateRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.user.UserRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.ratingService.ratingServiceInterface.RatingServiceInterface;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class RatingServiceImplement implements RatingServiceInterface {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private RateRepository rateRepository;

    private User checkTutorIsTrue(Long tutorId){
        User tutor = userRepository.findByIdAndStatusIsTrue(tutorId)
                .orElseThrow(()->{
                    throw new NoSuchElementException("User not found");
                });
        Boolean isTutor = false;
        for(Role role : tutor.getRoles()){
            isTutor = role.getUserRole().name().equals("TUTOR");
        }

        if(!isTutor){
            throw new IllegalArgumentException("User is not tutor");
        }
        return tutor;
    }

    @Override
    public List<Rate> getAllRating(Long tutorId) {
        User tutor = checkTutorIsTrue(tutorId);
        List<Rate> getListRate = rateRepository.findAllByStatusIsTrueAndTutor(tutor);
        return getListRate;
    }

    @Override
    public List<Rate> getTutorRatingBySubject(Long tutorId, Long subjectId) {
        User tutor = checkTutorIsTrue(tutorId);
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(()->{
                    throw new NoSuchElementException("Subject not found");
                });
        List<Rate> rateList = rateRepository.findAllByStatusIsTrueAndTutorAndSubject(tutor,subject);
        return rateList;
    }

    @Override
    public void deleteRating(String accessToken, Long tutorId, Long ratingId) {
        accessToken = accessToken.replaceAll("Bearer ","");
        User student = userRepository.findByAuthorizationToken(accessToken)
                .orElseThrow(()->{
                    throw new NoSuchElementException("User not found");
                });
        User tutor = checkTutorIsTrue(tutorId);
        Rate rate = rateRepository.findByIdAndStatusIsTrue(ratingId)
                .orElseThrow(() ->{
                   throw new NoSuchElementException("Rating not found");
                });
        if(student.getId() != rate.getStudent().getId()){
            throw new IllegalArgumentException("You are not allow to delete this rating");
        }
        rate.setStatus(false);
        rateRepository.save(rate);
    }
}
