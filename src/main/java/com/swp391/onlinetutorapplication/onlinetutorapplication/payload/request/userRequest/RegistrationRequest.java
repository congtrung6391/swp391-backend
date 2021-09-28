package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.userRequest;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegistrationRequest {

    @NotBlank
    @Size(min = 3, max = 25)
    private String username;

    @NotBlank
    @Size(max = 50)
    private String fullName;

    @NotBlank
    @Size(max = 50)
    private String email;

    @Size(max = 15)
    private String phone;

    @NotBlank
    @Size(min = 6, max = 30)
    private String password;

    private Set<String> role;



}
