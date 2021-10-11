package com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceInterface;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.Role;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.userRequest.UpdateProfileRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.userResponse.TutorListResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.userResponse.UserInformationResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.userResponse.UserProfileResponse;

import java.util.List;

public interface UserManagementInterface {
    User getUser(String username);

    User getUserById(Long id);

    UserProfileResponse getUserProfile(Long id);

    List<UserInformationResponse> getAllUser();

    void saveUser(User user);

    void updateUser(String accessToken, Long id, UpdateProfileRequest updateProfileRequest);

    void createAccount(User user);

    void saveRole(Role role);

    void deleteUser(Long id)throws Exception;

    List<TutorListResponse> getListTutor();
}
