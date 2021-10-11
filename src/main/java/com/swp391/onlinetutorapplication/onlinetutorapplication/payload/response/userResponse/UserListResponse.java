package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.userResponse;



import java.util.List;

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
