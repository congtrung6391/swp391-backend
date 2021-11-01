package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.forumResponse.answerResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Answer;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.userResponse.UserInformationResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerInformationResponse {
    private Long id;
    private String content;
    private LocalDate createdDate;
    private Long questionId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long replyId;
    private UserInformationResponse user;

    public AnswerInformationResponse(Answer answer){
        this.id = answer.getId();
        this.content = answer.getContent();
        this.createdDate = answer.getCreatedDate();
        this.questionId = answer.getQuestion().getId();
        this.user = new UserInformationResponse(answer.getUser());
        if(answer.getReplyId() != null){
            this.replyId = answer.getReplyId().getId();
        }
    }
}
