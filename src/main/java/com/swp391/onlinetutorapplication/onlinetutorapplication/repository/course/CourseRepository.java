package com.swp391.onlinetutorapplication.onlinetutorapplication.repository.course;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long>{

    Optional<Course> findByCourseDescription(String courseDes);
    Optional<Course> findByCourseName(String courseName);
    Optional<Course> findByCourseNameAndCourseDescription(String courseName, String courseDes);
    Optional<Course> findBySubject(String subject);
    Optional<Course> findByCost(double cost);
    Optional<Course> findByCostAndSubject(double cost, String subject);
    Optional<Course> findByIdAndCourseStatusIsTrue(Long id);

    @Override
    List<Course> findAll();
    List<Course> findAllByCourseStatusIsTrue();
}