package com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceInterface;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.Role;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.resetPasswordRequest.ResetPasswordRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.userRequest.LoginRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.userRequest.RegistrationRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.userRequest.UpdateProfileRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.JwtResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.MessageResponse;

import javax.mail.MessagingException;
import java.util.List;

public interface UserManagementInterface {
    User getUser(String username);

    User getUserById(Long id);

    List<User> getAllUser();

    void saveUser(User user);

    void updateUser(Long id, UpdateProfileRequest updateProfileRequest);

    void createAccount(User user);

    void saveRole(Role role);

    void deleteUser(Long id,String accessToken) throws Exception;
}
