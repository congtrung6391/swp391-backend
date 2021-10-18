package com.swp391.onlinetutorapplication.onlinetutorapplication.repository.course;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.CourseTimetable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseTimeTableRepository extends JpaRepository<CourseTimetable, Long> {
}
