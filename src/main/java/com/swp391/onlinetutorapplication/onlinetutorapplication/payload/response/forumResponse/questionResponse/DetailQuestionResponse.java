package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.forumResponse.questionResponse;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Answer;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Question;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.userResponse.UserInformationResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailQuestionResponse {
    private Boolean status = true;
    private Long id;
    private String title;
    private String description;
    private LocalDate createdDate;
    private List<Answer> answer;
    private UserInformationResponse userInformationResponse;

    public DetailQuestionResponse(Question question){
        this.id = question.getId();
        this.title = question.getTitle();
        this.description = question.getDescription();
        this.answer = question.getAnswer();
        this.createdDate = question.getCreatedDate();
        this.userInformationResponse = new UserInformationResponse(question.getUser());
    }
}
