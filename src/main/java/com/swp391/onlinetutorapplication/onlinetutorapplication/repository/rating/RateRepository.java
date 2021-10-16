package com.swp391.onlinetutorapplication.onlinetutorapplication.repository.rating;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Subject;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.rating.Rate;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {
    List<Rate> findAllByStatusIsTrueAndTutor(User tutor);

    List<Rate> findAllByStatusIsTrueAndTutorAndSubject(User tutor, Subject subject);


    Optional<Rate> findByIdAndStatusIsTrue(Long id);
}
