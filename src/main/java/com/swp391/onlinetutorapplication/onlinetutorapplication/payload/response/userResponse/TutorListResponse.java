package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.userResponse;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutorListResponse {
    private Boolean status = true;
    private int totalUser;
    List<UserInformationResponse> tutorList;

    public TutorListResponse(List<UserInformationResponse> tutorList) {
        this.totalUser = tutorList.size();
        this.tutorList = tutorList;
    }
}
