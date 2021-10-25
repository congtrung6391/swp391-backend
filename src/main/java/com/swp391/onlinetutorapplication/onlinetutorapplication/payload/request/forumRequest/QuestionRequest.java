package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.forumRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequest {
    private String title;
    private String description;
    private Long subjectId;
}
