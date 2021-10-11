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
    private Boolean status;
    private int totalUser;
    List<UserInformationResponse> userList;

    public UserListResponse(Boolean status, List<UserInformationResponse> userList) {
        this.status = status;
        this.totalUser = userList.size();
        this.userList = userList;
    }
}
