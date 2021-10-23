package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.forumResponse.questionResponse;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Question;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.userResponse.UserInformationResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionSearchResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDate createdDate;
    private UserInformationResponse userInformationResponse;

    public QuestionSearchResponse(Question question){
        this.id = question.getId();
        this.title = question.getTitle();
        this.createdDate = question.getCreatedDate();
        this.userInformationResponse = new UserInformationResponse(question.getUser());
    }
}
