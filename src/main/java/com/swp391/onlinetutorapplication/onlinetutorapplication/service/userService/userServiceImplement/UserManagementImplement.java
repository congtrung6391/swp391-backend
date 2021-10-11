package com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceImplement;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Course;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.Role;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.ERole;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.userRequest.UpdateProfileRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse.CourseInformationResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.userResponse.UserInformationResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.userResponse.UserProfileResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.role.RoleRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.user.UserRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceInterface.UserManagementInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.validation.constraints.Null;
import java.util.*;


@Service
@Slf4j
public class UserManagementImplement implements UserManagementInterface{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public User getUser(String username) {
        log.info("Fetching user {} ", username);
        return userRepository.findByUsername(username).orElseThrow(() ->{
           throw new NoSuchElementException("User cannot be found.");
        });
    }

    @Override
    public User getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> {
                    throw new NoSuchElementException("Not found user");
                });
        return user;
    }

    @Override
    public UserProfileResponse getUserProfile(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> {
                    throw new NoSuchElementException("Not found user");
                });
        return new UserProfileResponse(
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getGrade(),
                user.getAddress(),
                user.getFacebookUrl(),
                user.getAffiliate(),
                user.getGpa(),
                user.getGender()
        );
    }

    @Override
    public void saveUser(User user) { userRepository.save(user);}

    @Override
    public void updateUser(String accessToken, Long id, UpdateProfileRequest updateProfileRequest) {
        accessToken = accessToken.replaceAll("Bearer ","");
        User user = userRepository.findByAuthorizationToken(accessToken)
                .orElseThrow(()-> {
                    throw new NoSuchElementException("Not found user");
                });
        if(user.getId() != id){
            throw new IllegalStateException("You are not allowed to change other people's accounts");
        }
        if(!updateProfileRequest.getEmail().equals(user.getEmail())){
            if(updateProfileRequest.getEmail().isEmpty()){
                throw new IllegalStateException("Email not null");
            }
           user.setEmail(updateProfileRequest.getEmail());
        }
        if(!updateProfileRequest.getPhone().equals(user.getPhone())){
            if(updateProfileRequest.getPhone().isEmpty()){
                user.setPhone(null);
            }
            user.setPhone(updateProfileRequest.getPhone());
        }
        if(!updateProfileRequest.getFullName().equals(user.getFullName())){
            user.setFullName(updateProfileRequest.getFullName());
        }
        if(!updateProfileRequest.getGrade().equals(user.getGrade())){
            user.setGrade(updateProfileRequest.getGrade());
        }
        if(!updateProfileRequest.getAddress().equals(user.getAddress())){
            user.setAddress(updateProfileRequest.getAddress());
        }

        if(!updateProfileRequest.getAffiliate().equals(user.getAffiliate())){
            user.setAffiliate(updateProfileRequest.getAffiliate());
        }

        if(!updateProfileRequest.getFacebookUrl().equals(user.getFacebookUrl())){
            user.setFacebookUrl(updateProfileRequest.getFacebookUrl());
        }

        if(!updateProfileRequest.getGender().equals(user.getGender())){
            user.setGender(updateProfileRequest.getGender());
        }
        if(!updateProfileRequest.getGpa().equals(user.getGpa())){
            user.setGpa(updateProfileRequest.getGpa());
        }
        userRepository.save(user);

    }

    @Override
    public List<UserInformationResponse> getAllUser() {
        List<User> users = userRepository.findAllByStatusIsTrue();
        List<UserInformationResponse> userList = new ArrayList<>();
        for (User user : users) {
            UserInformationResponse response = new UserInformationResponse(user);
            userList.add(response);
        }
        return userList;
    }

    @Override
    public void createAccount(User user) {
        userRepository.save(user);
    }

    @Override
    public void saveRole(Role role) {
        roleRepository.save(role);
    }

    @Override
    public void deleteUser(Long id)throws Exception {
        User user = userRepository.findById(id).get();
        Role role = roleRepository.findByUserRole(ERole.SUPER_ADMIN).get();
        if (user.getRoles().contains(role)) {
            throw new Exception();
        }
        else {
            user.setStatus(false);
            userRepository.save(user);
        }
    }

}


