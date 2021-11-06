package com.swp391.onlinetutorapplication.onlinetutorapplication.repository.course;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Course;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.CourseStudent;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseStudentRepository extends JpaRepository<CourseStudent, Long> {
    CourseStudent findByCourseAndStudent(Course course, User student);

    CourseStudent findByCourse(Course course);

    List<CourseStudent> findAllByCourse(Course course);

    Page<CourseStudent> findAllByCourseOrderById(Course course, Pageable pageable);

    CourseStudent findByIdAndCourse(Long id,Course course);

    @Query("select cs from CourseStudent cs " +
            "join User u on cs.student = u and  u.id = ?1  " +
            "where cs.course = ?2 " +
            "order by cs.id desc ")
    Page<CourseStudent> findListByStudentId(Long studentId,Course course, Pageable pageable);

    @Query("select cs from CourseStudent cs " +
            "join User u on cs.student = u and  u.fullName like ?1  " +
            "where cs.course = ?2 " +
            "order by cs.id desc ")
    Page<CourseStudent> findByStudentName(String studentName,Course course, Pageable pageable);

    @Query("select cs from CourseStudent cs " +
            "join User u on cs.student = u and  u.fullName like ?1 and u.id =?2 " +
            "where cs.course = ?3 " +
            "order by cs.id desc ")
    Page<CourseStudent> findByStudentNameAndId(String studentName,Long studentId,Course course, Pageable pageable);
}
