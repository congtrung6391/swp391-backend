package com.swp391.onlinetutorapplication.onlinetutorapplication.service.forumService.forumServiceInterface;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Answer;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.forumRequest.AnswerUpdateRequest;

import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.forumRequest.AnswerCreateRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.forumResponse.answerResponse.AnswerInformationResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.forumResponse.answerResponse.AnswerListResponse;

public interface AnswerServiceInterface {
    AnswerInformationResponse createAnswer(String accessToken, AnswerCreateRequest request, Long questionId);

    AnswerListResponse getAnswerList(Long id, Integer page, Integer limit);

    void deleteAnswer(Long questionId, Long answerId, String accessToken) throws Exception;
    Answer updateAnswer(AnswerUpdateRequest request, Long questionId, Long answerId, String accessToken) throws Exception;
}
