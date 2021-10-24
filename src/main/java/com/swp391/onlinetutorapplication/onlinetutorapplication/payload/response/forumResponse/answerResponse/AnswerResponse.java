package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.forumResponse.answerResponse;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Answer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerResponse {
    private AnswerInformation answer;
    private boolean status = true;

    public AnswerResponse(Answer answer) { this.answer = new AnswerInformation(answer); }
}
