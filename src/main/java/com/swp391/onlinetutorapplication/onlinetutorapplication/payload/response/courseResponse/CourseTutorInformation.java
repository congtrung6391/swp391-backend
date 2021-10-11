package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CourseTutorInformation {
    private Long id;
    private String fullName;
    private String email;
    private String phone;

    public CourseTutorInformation(User tutor){
        this.id = tutor.getId();
        this.fullName = tutor.getFullName();
        this.email = tutor.getEmail();
        this. phone = tutor.getPhone();
    }
}
