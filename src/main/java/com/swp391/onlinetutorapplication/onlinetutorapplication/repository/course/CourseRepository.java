package com.swp391.onlinetutorapplication.onlinetutorapplication.repository.course;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Course;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.CourseStudent;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {


    Optional<Course> findByIdAndTutorAndStatusIsTrue(Long id, User tutor);

    Optional<Course> findById(Long id);


    Optional<Course> findByIdAndStatusIsTrue(Long id);


    Optional<Course> findByIdAndPublicStatusIsTrue(Long id);


    @Query("select c from Course c join CourseStudent cs on c = cs.course and cs.student = ?1 order by c.id desc ")
    Page<Course> findByStudentRole(User student, Pageable pageable);
}
