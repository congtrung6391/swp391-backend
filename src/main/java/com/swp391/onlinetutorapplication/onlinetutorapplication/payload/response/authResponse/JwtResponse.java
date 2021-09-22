package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class JwtResponse {
    private String access_token;
    private String refresh_token;
    private String type = "Bearer ";
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private String errorMessage;

    public JwtResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public JwtResponse(String accessToken, String refresh_token, Long id, String username, String email, List<String> roles) {
        this.access_token = accessToken;
        this.refresh_token = refresh_token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
