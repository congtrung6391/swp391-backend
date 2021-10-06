package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.courseRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CourseUpdateRequest {
    private String title;
    private String description;
    private Double cost;
    private Integer grade;
    private Integer length;
    private Long subjectId;
}
