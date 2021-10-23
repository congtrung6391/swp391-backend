package com.swp391.onlinetutorapplication.onlinetutorapplication.repository.forum;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Subject;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {
    List<Question> findAllByStatusIsTrueOrderByIdDesc(Pageable pageable);
    List<Question> findAllByStatusIsTrueAndTitleContainingOrderByIdDesc(String name, Pageable pageable);
    List<Question> findAllByStatusIsTrueAndSubjectOrderByIdDesc(Subject subject, Pageable pageable);
    List<Question> findAllByStatusIsTrueAndTitleContainingAndSubjectOrderByIdDesc(String name, Subject subject, Pageable pageable);

    Optional<Question> findByIdAndStatusIsTrue(Long questionId);
}
