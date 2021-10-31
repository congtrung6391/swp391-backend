package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.forumResponse.answerResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Answer;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Question;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.userResponse.UserInformationResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerInformation {
    private Boolean status = true;
    private Long id;
    private String content;
    private LocalDate createdDate;
    private Long questionId;
    private User user;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long replyId;

    public AnswerInformation(Answer answer) {
        this.id = answer.getId();
        this.content = answer.getContent();
        this.createdDate = answer.getCreatedDate();
        this.questionId = answer.getQuestion().getId();
        this.user = answer.getUser();
        if (answer.getReplyId() != null) {
            this.replyId = answer.getReplyId().getId();
        }
    }
}
