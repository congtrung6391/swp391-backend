package com.swp391.onlinetutorapplication.onlinetutorapplication.repository.course;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Course;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByIdAndLearningStatusIsTrue(Long id);

    Optional<Course> findByIdAndTutorAndLearningStatusIsTrue(Long id, User tutor);

    Optional<Course> findById(Long id);

    Optional<Course> findByIdAndStudentIsNotNullAndPublicStatusIsTrue(Long id);

    Optional<Course> findByIdAndStatusIsTrue(Long id);

    Page<Course> findAllByStudentAndStatusIsTrueOrderByIdDesc(User student, Pageable pageable);

    Page<Course> findAllByStudentIsNullAndPublicStatusIsTrueOrderByIdDesc(Pageable pageable);

    Optional<Course> findByIdAndPublicStatusIsTrueAndStudentIsNull(Long id);

    Optional<Course> findByIdAndPublicStatusIsTrue(Long id);

    Optional<Course> findByIdAndPublicStatusIsFalse(Long id);

    Optional<Course> findByIdAndLearningStatusIsFalse(Long id);
}
