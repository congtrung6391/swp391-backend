package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.userResponse;



import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse.MaterialCreationResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
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

    public UserListResponse(List<UserInformationResponse> userList, int page, int limit) {
        this.totalUser = userList.size();
        List<UserInformationResponse> responses = new ArrayList<>();
        int index = page*limit;
        int des = userList.size()<(index+limit) ? userList.size() : index+limit;
        for(int i = index; i< des ; i++){
            responses.add(userList.get(i));
        }
        this.userList = responses;
    }
}
