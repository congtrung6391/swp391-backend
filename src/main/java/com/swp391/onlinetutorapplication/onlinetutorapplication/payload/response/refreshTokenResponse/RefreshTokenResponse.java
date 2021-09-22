package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.refreshTokenResponse;

import lombok.Data;

@Data
public class RefreshTokenResponse {
    private String access_token;
    private String refresh_token;
    private String tokenType = "Bearer ";

    public RefreshTokenResponse(String access_token, String refresh_token) {
        this.access_token = access_token;
        this.refresh_token = refresh_token;
    }
}
