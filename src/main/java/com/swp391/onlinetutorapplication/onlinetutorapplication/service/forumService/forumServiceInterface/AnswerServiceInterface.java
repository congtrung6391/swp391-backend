package com.swp391.onlinetutorapplication.onlinetutorapplication.service.forumService.forumServiceInterface;

import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.forumRequest.AnswerCreateRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.forumResponse.answerResponse.AnswerInformationResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.forumResponse.answerResponse.AnswerListResponse;

public interface AnswerServiceInterface {
    AnswerInformationResponse createAnswer(String accessToken, AnswerCreateRequest request, Long questionId);
    AnswerListResponse getAnswerList(Long id, Integer page, Integer limit);
}
