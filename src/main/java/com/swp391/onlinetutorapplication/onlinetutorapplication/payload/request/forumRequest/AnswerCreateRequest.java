package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.forumRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerCreateRequest {
    private Long replyId;
    private String content;
}
