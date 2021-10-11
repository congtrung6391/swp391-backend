package com.swp391.onlinetutorapplication.onlinetutorapplication.repository.course;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Course;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByCourseDescription(String courseDes);

    Optional<Course> findByCourseName(String courseName);

    Optional<Course> findByCourseNameAndCourseDescription(String courseName, String courseDes);

    Optional<Course> findBySubject(String subject);

    Optional<Course> findByCost(double cost);

    Optional<Course> findByCostAndSubject(double cost, String subject);

    Optional<Course> findByIdAndCourseStatusIsTrue(Long id);

    Optional<Course> findByIdAndTutor(Long id, User tutor);

    Optional<Course> findById(Long id);

    List<Course> findAllByTutorAndStatusIsTrue(User tutor);

    List<Course> findAllByStatusIsTrue();

    Optional<Course> findByIdAndStatusIsTrue(Long id);

    Optional<Course> findByIdAndTutorAndStatusIsTrue(Long id, User tutor);

    Optional<Course> findByIdAndStudentAndStatusIsTrue(Long id, User student);

    @Override
    List<Course> findAll();

    List<Course> findAllByStudentIsNullAndCourseStatusIsTrueAndStatusIsTrue();
}
