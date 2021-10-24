package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.forumResponse.questionResponse;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponse {
    private boolean status = true;
    private DetailQuestionResponse questionResponse;

    public QuestionResponse(Question questionResponse) {
        this.questionResponse = new DetailQuestionResponse(questionResponse);
    }
}