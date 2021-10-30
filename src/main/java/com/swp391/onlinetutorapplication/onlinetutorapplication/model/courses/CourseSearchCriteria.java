package com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseSearchCriteria {
    private Long id;
    private String courseName;
    private Long subjectId;
    private String tutorName;
}
