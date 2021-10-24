package com.swp391.onlinetutorapplication.onlinetutorapplication.service.forumService.forumServiceInterface;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Answer;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.forumRequest.AnswerUpdateRequest;

public interface AnswerServiceInterface {
    void deleteAnswer(Long questionId, Long answerId, String accessToken) throws Exception;
    Answer updateAnswer(AnswerUpdateRequest request, Long questionId, Long answerId, String accessToken);
}
