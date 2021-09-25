package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.resetPasswordRequest;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResetPasswordRequest {
    private Long resetCode;
    @NotBlank
    @Size(min = 6, max = 30)
    private String newPassword;
}
