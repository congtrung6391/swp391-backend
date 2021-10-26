package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.forumResponse.answerResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerResponse {
    private boolean status = true;
    private AnswerInformationResponse answer;

    public AnswerResponse(AnswerInformationResponse answer){
        this.answer = answer;
    }
}
