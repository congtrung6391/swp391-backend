package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.userResponse;


import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.Role;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserInformationResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private Set<Role> role;

    public UserInformationResponse(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
        this.role = user.getRoles();
    }
}
