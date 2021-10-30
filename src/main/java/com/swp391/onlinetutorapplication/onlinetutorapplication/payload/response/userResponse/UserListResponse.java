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
    private Long totalUser;
    List<UserInformationResponse> userList;

    public UserListResponse(List<UserInformationResponse> userList) {
        this.userList = userList;
    }


}
