package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.userRequest;

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


    private String fullName;


    private String email;

    @Size(max = 15)
    private String phone;

    private Integer grade;

    private String address;

    private String facebookUrl;
    private String affiliate;

    private Double gpa;

    private String gender;
}
