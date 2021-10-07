package com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceImplement;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.Role;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.userRequest.UpdateProfileRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.role.RoleRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.user.UserRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceInterface.UserManagementInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
        return userRepository.findByUsername(username).get();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public void saveUser(User user) { userRepository.save(user);}

    @Override
    public void updateUser(Long id, UpdateProfileRequest updateProfileRequest) {
        User userFromDb = userRepository.findById(id).get();
        System.out.println(userFromDb.toString());
        userFromDb.setPhone(updateProfileRequest.getPhone());
        userFromDb.setEmail(updateProfileRequest.getEmail());
        userFromDb.setFullName(updateProfileRequest.getFullName());
        userRepository.save(userFromDb);
    }

    @Override
    public List<User> getAllUser() {
        log.info("Fetching all users");
        return userRepository.findAll();
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
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).get();
        user.setIsDisable(true);
        userRepository.save(user);
    }
}


