package com.swp391.onlinetutorapplication.onlinetutorapplication.repository.course;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Course;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Subject;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByIdAndCourseStatusIsTrue(Long id);

    Optional<Course> findByIdAndTutor(Long id, User tutor);

    Optional<Course> findById(Long id);

    Optional<Course> findByIdAndStudentIsNotNullAndCourseStatusIsTrue(Long id);

    List<Course> findAllByTutorAndStatusIsTrueOrderByIdDesc(User tutor, Pageable pageable);

    @Query("SELECT c FROM Course c WHERE " +
            "(c.id = ?1 " +
            "OR c.courseName LIKE %?2% " +
            "OR c.subject.subjectName = ?3 " +
            "OR c.tutor.fullName LIKE %?4%) " +
            "AND c.status = true ORDER BY c.id DESC")
    List<Course> findAllByStatusIsTrueOrderByIdDesc(Long id, String courseName, String subjectName, String fullName, Pageable pageable);

    List<Course> findAllByStatusIsTrueOrderByIdDesc(Pageable pageable);

    Optional<Course> findByIdAndStatusIsTrue(Long id);

    List<Course> findAllByStudentAndStatusIsTrueOrderByIdDesc(User student, Pageable pageable);

    List<Course> findAllByStudentIsNullAndCourseStatusIsTrueAndStatusIsTrueOrderByIdDesc(Pageable pageable);
}
