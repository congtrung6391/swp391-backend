package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.SuperAdminRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChangeRoleUserRequest {
    private String role;
}
