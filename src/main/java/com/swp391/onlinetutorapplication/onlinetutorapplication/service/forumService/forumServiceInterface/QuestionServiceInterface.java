package com.swp391.onlinetutorapplication.onlinetutorapplication.service.forumService.forumServiceInterface;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Question;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.forumRequest.QuestionRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.forumResponse.questionResponse.ListQuestionResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface QuestionServiceInterface {
    ListQuestionResponse getListQuestion(Integer page, Integer limit);

    ListQuestionResponse getListQuestionByNameOrSubject(String name, Long subjectId, Integer page, Integer limit);

    Question getDetailsQuestion(Long questionId);

    void deleteQuestion(String accessToken, Long questionId);

    Question createQuestion(QuestionRequest questionRequest, String accesstoken);

    Question updateQuestion(QuestionRequest questionRequest, String accessToken, Long questionId) throws Exception;
}
