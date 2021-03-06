package com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceInterface;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.Role;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.userRequest.PasswordUpdateRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.userRequest.UpdateProfileRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.userResponse.TutorListResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.userResponse.UserListResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.userResponse.UserProfileResponse;

public interface UserManagementInterface {
    User getUser(String username);

    User getUserById(Long id);

    UserProfileResponse getUserProfile(Long id);

    UserListResponse getAllUser(Integer page, Integer limit);

    void saveUser(User user);

    User updateUser(String accessToken, Long id, UpdateProfileRequest updateProfileRequest) ;

    User updateUserPassword(String accessToken, Long id, PasswordUpdateRequest request) throws Exception;

    void createAccount(User user);

    void saveRole(Role role);

    void deleteUser(Long id) throws Exception;

    TutorListResponse getListTutor(Integer page, Integer limit);

    //admin search user - by Nam
    UserListResponse adminSearchUser(String id, String name, Integer page, Integer limit);

    //    Object publicSearchUser(String name);
    TutorListResponse publicSearchTutor(String name, Integer page, Integer limit);

    TutorListResponse getListTutorOrderByRating(Integer page, Integer limit, String order);
}
