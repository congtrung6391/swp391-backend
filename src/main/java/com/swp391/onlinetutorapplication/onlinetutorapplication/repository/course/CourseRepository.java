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

    Optional<Course> findByIdAndStudentIsNotNullAndLearningStatusIsTrue(Long id);

    Page<Course> findAllByTutorAndStatusIsTrueOrderByIdDesc(User tutor, Pageable pageable);

//    @Query("SELECT c FROM Course c WHERE " +
//            "(c.id = ?1 " +
//            "OR c.courseName LIKE %?2% " +
//            "OR c.subject.id = ?3 " +
//            "OR c.tutor.fullName LIKE %?4%) " +
//            "AND c.status = true ORDER BY c.id DESC")
//    List<Course> findAllByStatusIsTrueOrderByIdDesc(Long id, String courseName, Long subjectId, String fullName, Pageable pageable);

    Page<Course> findAllByStatusIsTrueOrderByIdDesc(Pageable pageable);

    Optional<Course> findByIdAndStatusIsTrue(Long id);

    Page<Course> findAllByStudentAndStatusIsTrueOrderByIdDesc(User student, Pageable pageable);

    Page<Course> findAllByStudentIsNullAndPublicStatusIsTrueOrderByIdDesc(Pageable pageable);

    Optional<Course> findByIdAndPublicStatusIsTrue(Long id);

    Optional<Course> findByIdAndPublicStatusIsFalse(Long id);

    Optional<Course> findByIdAndLearningStatusIsFalse(Long id);
}
