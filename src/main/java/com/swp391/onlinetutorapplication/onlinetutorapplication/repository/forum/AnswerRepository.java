package com.swp391.onlinetutorapplication.onlinetutorapplication.repository.forum;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Answer;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findByIdAndStatusIsTrue(Long id);

    Optional<List<Answer>> findAllByQuestionAndStatusIsTrue(Question question, Pageable page);

    List<Answer> findAllByQuestionAndStatusIsTrue(Question question);

    Optional<Answer> findByIdAndStatusIsTrueAndUserIsNotNull(Long id);
}
