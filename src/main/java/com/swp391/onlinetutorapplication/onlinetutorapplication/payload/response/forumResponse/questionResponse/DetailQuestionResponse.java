package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.forumResponse.questionResponse;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Subject;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Answer;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Question;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.forumResponse.answerResponse.AnswerListResponse;
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
    private Subject subject;
    private UserInformationResponse author;

    public DetailQuestionResponse(Question question){
        this.id = question.getId();
        this.title = question.getTitle();
        this.description = question.getDescription();
        this.createdDate = question.getCreatedDate();
        this.subject= question.getSubject();
        this.author = new UserInformationResponse(question.getUser());
    }
}
