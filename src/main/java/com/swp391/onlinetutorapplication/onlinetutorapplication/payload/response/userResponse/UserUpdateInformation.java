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
public class UserUpdateInformation {
    private Long id;
    private String fullName;
    private String phone;
    private Integer grade;
    private String address;
    private String avatar;
    private String facebookUrl;
    private String affiliate;
    private Double gpa;
    private String gender;
    private String birthday;

    public UserUpdateInformation(User user){
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.phone = user.getPhone();
        this.grade = user.getGrade();
        this.address = user.getAddress();
        this.avatar = user.getAvatar();
        this.facebookUrl = user.getFacebookUrl();
        this.affiliate = user.getAffiliate();
        this.gpa = user.getGpa();
        this.gender = user.getGender();
        this.birthday = user.getBirthday();
    }
}
