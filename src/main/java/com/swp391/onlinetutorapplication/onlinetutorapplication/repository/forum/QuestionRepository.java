package com.swp391.onlinetutorapplication.onlinetutorapplication.repository.forum;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question,Long> {
}
