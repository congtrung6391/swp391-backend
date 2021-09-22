package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.resetPasswordRequest;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Data
public class ResetPasswordRequest {
    @NotBlank
    @Size(min = 6, max = 30)
    String newPassword;
}
