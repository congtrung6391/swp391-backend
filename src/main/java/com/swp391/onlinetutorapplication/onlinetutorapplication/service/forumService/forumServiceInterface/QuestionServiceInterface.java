package com.swp391.onlinetutorapplication.onlinetutorapplication.service.forumService.forumServiceInterface;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Question;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.forumRequest.QuestionRequest;
import org.springframework.stereotype.Service;

import java.util.List;

public interface QuestionServiceInterface {
    List<Question> getListQuestion(Integer page, Integer limit);

    List<Question> getListQuestionByNameOrSubject(String name, Long subjectId, Integer page, Integer limit);

    Question getDetailsQuestion(Long questionId);

    void deleteQuestion(String accessToken, Long questionId);

    Question createQuestion(QuestionRequest questionRequest, String accesstoken);

    Question updateQuestion(QuestionRequest questionRequest, String accessToken, Long questionId) throws Exception;
}
