package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.forumResponse.questionResponse;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListQuestionResponse {
    private Boolean status = true;
    private Integer totalQuestion;
    private List<QuestionSearchResponse> listQuestion;

    public ListQuestionResponse(List<Question> listQuestion){
        this.totalQuestion = listQuestion.size();
        List<QuestionSearchResponse> responseList = new ArrayList<>();
        for(Question question: listQuestion){
            QuestionSearchResponse response = new QuestionSearchResponse(question);
            responseList.add(response);
        }
        this.listQuestion = responseList;
    }
}
