package com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminCourseSearchCriteria {
    private Long userId;
    private Role role;
    private Long id;
    private String courseName;
    private Long subjectId;
    private String tutorName;
}
