package com.swp391.onlinetutorapplication.onlinetutorapplication.service.ratingService.ratingServiceImplement;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Subject;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.rating.Rate;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.Role;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.ratingRequest.AddRatingRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.ratingRequest.UpdateRatingRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.course.SubjectRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.rating.RateRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.user.UserRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.ratingService.ratingServiceInterface.RatingServiceInterface;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceInterface.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class RatingServiceImplement implements RatingServiceInterface {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceInterface userService;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private RateRepository rateRepository;

    private User checkTutorIsTrue(Long tutorId) {
        User tutor = userRepository.findByIdAndStatusIsTrue(tutorId)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("User not found");
                });
        Boolean isTutor = false;
        for (Role role : tutor.getRoles()) {
            isTutor = role.getUserRole().name().equals("TUTOR");
        }

        if (!isTutor) {
            throw new IllegalArgumentException("User is not tutor");
        }
        return tutor;
    }

    @Override
    public List<Rate> getAllRating(Long tutorId, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page-1,limit);
        User tutor = checkTutorIsTrue(tutorId);
        List<Rate> getListRate = rateRepository.findAllByStatusIsTrueAndTutorOrderByIdDesc(tutor,pageable);
        return getListRate;
    }

    @Override
    public List<Rate> getTutorRatingBySubject(Long tutorId, Long subjectId,Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page-1, limit);
        User tutor = checkTutorIsTrue(tutorId);
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("Subject not found");
                });
        List<Rate> rateList = rateRepository.findAllByStatusIsTrueAndTutorAndSubjectOrderByIdDesc(tutor, subject,pageable);
        return rateList;
    }

    @Override
    public void deleteRating(String accessToken, Long tutorId, Long ratingId) {
        accessToken = accessToken.replaceAll("Bearer ", "");
        User student = userRepository.findByAuthorizationToken(accessToken)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("User not found");
                });
        User tutor = checkTutorIsTrue(tutorId);
        Rate rate = rateRepository.findByIdAndStatusIsTrue(ratingId)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("Rating not found");
                });
        if (!student.getId().equals(rate.getStudent().getId())) {
            throw new IllegalArgumentException("You are not allow to delete this rating");
        }
        rate.setStatus(false);
        rateRepository.save(rate);
    }

    @Override
    public Rate addRating(String accessToken, Long tutorId, AddRatingRequest request) {
        accessToken = accessToken.replaceAll("Bearer ", "");
        User user = userRepository.findByAuthorizationToken(accessToken).orElseThrow(() -> {
            throw new NoSuchElementException("User not found");
        });
        if (user.getExpireAuthorization().isBefore(Instant.now())) {
            userService.handleUserLogout(accessToken);
        }
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> {
                    throw new NoSuchElementException("Not found subject");
                });
        User tutor = userRepository.findByIdAndStatusIsTrue(tutorId).orElseThrow(() -> {
            throw new NoSuchElementException("Tutor Not Found");
        });
        Rate rate = null;
        Set<Role> roles = tutor.getRoles();
        for (Role role : roles) {
            switch (role.getUserRole()) {
                case TUTOR:
                    rate = new Rate(request.getValue(),
                            request.getDescription(),
                            LocalDateTime.now(),
                            subject);
                    rate.setStudent(user);
                    rate.setTutor(tutor);
                    rateRepository.save(rate);
                    break;
                default:
                    throw new IllegalArgumentException("You can only rate tutor");
            }
        }
        return rate;
    }

    @Override
    public Rate updateRating(String accessToken, Long tutorId, Long ratingId, UpdateRatingRequest request) {
        accessToken = accessToken.replaceAll("Bearer ", "");
        User user = userRepository.findByAuthorizationToken(accessToken).orElseThrow(() -> {
            throw new NoSuchElementException("User not found");
        });
        if (user.getExpireAuthorization().isBefore(Instant.now())) {
            userService.handleUserLogout(accessToken);
        }
        User tutor = userRepository.findByIdAndStatusIsTrue(tutorId).orElseThrow(() -> {
            throw new NoSuchElementException("Tutor Not Found");
        });
        Rate rate = null;
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            switch (role.getUserRole()) {
                case ADMIN:
                case SUPER_ADMIN:
                    rate = rateRepository.findByIdAndTutorAndStatusIsTrue(ratingId, tutor).orElseThrow(() -> {
                        throw new NoSuchElementException("Rating Not Found");
                    });
                    rateRepository.save(rate);
                    break;
                case STUDENT:
                    rate = rateRepository.findByIdAndTutorAndStudentAndStatusIsTrue(ratingId, tutor, user).orElseThrow(() -> {
                        throw new NoSuchElementException("Rating Not Found");
                    });
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }

        if (request.getDescription() != null) {
            rate.setDescription(request.getDescription());
        }

        if (request.getValue() != null) {
            rate.setValue(request.getValue());
        }
        rateRepository.save(rate);
        return rate;
    }
}
