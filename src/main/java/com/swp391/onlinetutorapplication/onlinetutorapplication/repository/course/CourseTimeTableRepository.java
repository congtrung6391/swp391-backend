package com.swp391.onlinetutorapplication.onlinetutorapplication.repository.course;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Course;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.CourseTimetable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseTimeTableRepository extends JpaRepository<CourseTimetable, Long> {
    List<CourseTimetable> findAllByCourseAndStatusIsTrue(Course course);

    Optional<CourseTimetable> findByIdAndStatusIsTrue(Long aLong);
}
