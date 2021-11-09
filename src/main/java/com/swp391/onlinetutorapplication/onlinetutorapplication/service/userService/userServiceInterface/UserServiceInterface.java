package com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceInterface;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.resetPasswordRequest.ResetPasswordRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.userRequest.LoginRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.userRequest.RegistrationRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.JwtResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.MessageResponse;

import javax.mail.MessagingException;

public interface UserServiceInterface {
    JwtResponse handleUserLogin(LoginRequest loginRequest) throws Exception;

    void handleUserRegistration(RegistrationRequest registrationRequest) throws MessagingException;

    void activeAccount(String activateToken);

    void sendTokenForgetPassword(String email) throws MessagingException;

    User verifiedResetCode(Long resetCode);

    void resetPassword(ResetPasswordRequest resetPasswordRequest);

    void handleUserLogout(String accessToken);

    void verifyAccessToken(String accessToken);

    boolean changeRole(String username, String role);
}
