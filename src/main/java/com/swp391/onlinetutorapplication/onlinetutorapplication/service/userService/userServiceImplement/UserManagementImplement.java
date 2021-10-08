package com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceImplement;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.Role;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.refreshToken.RefreshToken;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.resetPasswordRequest.ResetPasswordRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.userRequest.UpdateProfileRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.utils.jwtUtils.JWTUtils;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.userRequest.LoginRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.userRequest.RegistrationRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.JwtResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.MessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.role.RoleRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.user.UserRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.userDetails.UserDetailsImplement;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.mailSenderService.MailSenderService;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceInterface.UserManagementInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

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


