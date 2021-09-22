package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.tokenRequest;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RefreshTokenRequest {
    @NotBlank
    String refreshToken;
}
