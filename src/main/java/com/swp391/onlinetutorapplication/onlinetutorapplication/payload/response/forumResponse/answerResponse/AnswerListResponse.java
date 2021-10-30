package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.forumResponse.answerResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerListResponse {
    private int totalAnswer;
    private boolean status = true;
    private List<AnswerInformationResponse> answerList;

    public AnswerListResponse(int totalAnswer, List<AnswerInformationResponse> answerList){
        this.totalAnswer = totalAnswer;
        this.answerList = answerList;
    }
}
