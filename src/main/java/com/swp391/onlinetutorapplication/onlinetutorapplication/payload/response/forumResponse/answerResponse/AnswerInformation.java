package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.forumResponse.answerResponse;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Answer;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Question;
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
    private Question question;
    private UserInformationResponse userInformationResponse;

    public AnswerInformation(Answer answer) {
        this.id = answer.getId();
        this.content = answer.getContent();
        this.createdDate = answer.getCreatedDate();
        this.question = answer.getQuestion();
        this.userInformationResponse = new UserInformationResponse(answer.getUser());
    }
}
