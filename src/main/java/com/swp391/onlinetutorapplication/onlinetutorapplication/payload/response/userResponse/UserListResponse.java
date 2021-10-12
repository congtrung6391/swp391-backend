package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.userResponse;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserListResponse {
    private Boolean status = true;
    private int totalUser;
    List<UserInformationResponse> userList;

    public UserListResponse(List<UserInformationResponse> userList) {
        this.totalUser = userList.size();
        this.userList = userList;
    }
}
