package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.userResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private String username;
    private String fullName;

    private String email;

    private String phone;

    private Integer grade;

    private String address;

    private String facebookUrl;

    private String affiliate;

    private Double gpa;

    private String gender;

    private Boolean status = true;

    public UserProfileResponse(String username, String fullName, String email, String phone, Integer grade, String address, String facebookUrl, String affiliate, Double gpa, String gender) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.grade = grade;
        this.address = address;
        this.facebookUrl = facebookUrl;
        this.affiliate = affiliate;
        this.gpa = gpa;
        this.gender = gender;
    }
}