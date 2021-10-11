package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.userResponse;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;

import java.util.List;

public class TutorListResponse {
    private Boolean status = true;
    private int totalUser;
    List<User> tutorList;

    public TutorListResponse(List<User> tutorList) {
        this.totalUser = tutorList.size();
        this.tutorList = tutorList;
    }
}
