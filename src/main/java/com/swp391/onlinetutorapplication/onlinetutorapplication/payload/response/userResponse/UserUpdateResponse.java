package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.userResponse;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateResponse {
    private UserUpdateInformation user;
    private boolean status = true;

    public UserUpdateResponse(User user){
        this.user = new UserUpdateInformation(user);
    }
}
