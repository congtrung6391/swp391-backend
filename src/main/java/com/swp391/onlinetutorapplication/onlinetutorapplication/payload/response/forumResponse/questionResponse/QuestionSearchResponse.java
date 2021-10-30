package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.forumResponse.questionResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Subject;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Question;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.userResponse.UserInformationResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionSearchResponse {
    private Long id;
    private String title;
    private String description;
    private Long numberOfAnswer;
    private LocalDate createdDate;
    private UserInformationResponse author;
    private Subject subject;

    public QuestionSearchResponse(Question question){
        this.id = question.getId();
        this.title = question.getTitle();
        this.description = question.getDescription().substring(0,
                (question.getDescription().length() > 200 ?
                        200 : question.getDescription().length()));
        this.createdDate = question.getCreatedDate();
        this.numberOfAnswer = Long.valueOf(question.getAnswer().size());
        this.author = new UserInformationResponse(question.getUser());
        this.subject = question.getSubject();
    }
}
