package com.swp391.onlinetutorapplication.onlinetutorapplication.repository.course;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.ESubject;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Optional<Subject> findBySubjectName(ESubject subject);



}
