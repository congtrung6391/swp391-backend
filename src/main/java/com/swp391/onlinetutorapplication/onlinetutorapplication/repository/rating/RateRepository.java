package com.swp391.onlinetutorapplication.onlinetutorapplication.repository.rating;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Subject;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.rating.Rate;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {
    Page<Rate> findAllByStatusIsTrueAndTutorOrderByIdDesc(User tutor, Pageable pageable);

    Page<Rate> findAllByStatusIsTrueAndTutorAndSubjectOrderByIdDesc(User tutor, Subject subject, Pageable pageable);

    List<Rate> findAllByStatusIsTrueAndTutorAndSubject(User tutor, Subject subject);

    List<Rate> findAllByStatusIsTrueAndTutor(User tutor);

    Optional<Rate> findByIdAndTutorAndStatusIsTrue(Long id, User tutor);

    Optional<Rate> findByIdAndTutorAndStudentAndStatusIsTrue(Long id, User tutor, User student);

    Optional<Rate> findByIdAndStatusIsTrue(Long id);
}
