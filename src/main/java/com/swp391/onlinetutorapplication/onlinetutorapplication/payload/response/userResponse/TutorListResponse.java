package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.userResponse;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse.MaterialCreationResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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

    public TutorListResponse(List<UserInformationResponse> tutorList, Integer page, Integer limit) {
        page = page-1;
        this.totalUser = tutorList.size();
        List<UserInformationResponse> responses = new ArrayList<>();
        int index = page*limit;
        int des = tutorList.size()<(index+limit) ? tutorList.size() : index+limit;
        for(int i = index; i< des ; i++){
            responses.add(tutorList.get(i));
        }
        this.tutorList = responses;
    }
}
