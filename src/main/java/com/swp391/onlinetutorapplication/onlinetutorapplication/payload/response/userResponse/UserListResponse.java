package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.userResponse;



import java.util.List;

public class UserListResponse {
    private Boolean status = true;
    private int totalUser;
    List<UserInformationResponse> userList;

    public UserListResponse(List<UserInformationResponse> userList) {
        this.totalUser = userList.size();
        this.userList = userList;
    }
}
