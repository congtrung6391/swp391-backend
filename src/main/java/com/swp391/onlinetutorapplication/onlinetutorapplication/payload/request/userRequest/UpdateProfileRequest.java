package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.userRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateProfileRequest {

    @JsonProperty(required = false)
    private String fullName;

    @JsonProperty(required = false)
    @Size(max = 15)
    private String phone;

    @JsonProperty(required = false)
    private Integer grade;

    @JsonProperty(required = false)
    private String address;

    @JsonProperty(required = false)
    private String avatar;

    @JsonProperty(required = false)
    private String facebookUrl;

    @JsonProperty(required = false)
    private String affiliate;

    @JsonProperty(required = false)
    private Double gpa;

    @JsonProperty(required = false)
    private String gender;

    @JsonProperty(required = false)
    private String birthday;

    /*
    @JsonProperty(required = false)
    private String password;

    @JsonProperty(required = false)
    private String newPassword;

     */
}
