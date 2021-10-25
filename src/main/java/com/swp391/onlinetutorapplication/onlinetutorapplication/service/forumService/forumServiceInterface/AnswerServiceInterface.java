package com.swp391.onlinetutorapplication.onlinetutorapplication.service.forumService.forumServiceInterface;

public interface AnswerServiceInterface {
    void deleteAnswer(Long questionId, Long answerId, String accessToken) throws Exception;
}
