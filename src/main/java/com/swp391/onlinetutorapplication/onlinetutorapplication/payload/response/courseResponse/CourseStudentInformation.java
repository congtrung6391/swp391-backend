package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.Role;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CourseStudentInformation {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private Set<Role> role;

    public CourseStudentInformation(User student){
        this.id = student.getId();
        this.fullName = student.getFullName();
        this.email = student.getEmail();
        this.phone = student.getPhone();
        this.role = student.getRoles();
    }
}
