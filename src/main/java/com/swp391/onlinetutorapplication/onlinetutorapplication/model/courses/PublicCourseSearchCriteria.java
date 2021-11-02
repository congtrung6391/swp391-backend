package com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PublicCourseSearchCriteria {
    private String courseName;
    private Long subjectId;
    private Long minCost;
    private Long maxCost;
    private int minLength;
    private int maxLength;
    private String tutorName;
}
