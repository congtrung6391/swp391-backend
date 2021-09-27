package com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceInterface;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.Role;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.resetPasswordRequest.ResetPasswordRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.userRequest.LoginRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.userRequest.RegistrationRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.JwtResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.MessageResponse;

import javax.mail.MessagingException;
import java.util.List;

public interface UserServiceInterface {
    User getUser(String username);
    List<User> getAllUser();
    void createAccount(User user);
    void saveRole(Role role);
    JwtResponse handleUserLogin(LoginRequest loginRequest) throws Exception;
    MessageResponse handleUserRegistration(RegistrationRequest registrationRequest) throws MessagingException;
     void activeAccount(String activateToken);
     void sendTokenForgetPassword(String email) throws MessagingException;
     User verifiedResetCode(Long resetCode);
     void resetPassword(ResetPasswordRequest resetPasswordRequest);
     boolean changeRole(String role, String username);
}
