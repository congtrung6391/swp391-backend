package com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceImplement;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.ERole;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.Role;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.refreshToken.RefreshToken;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.resetPasswordRequest.ResetPasswordRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.utils.jwtUtils.JWTUtils;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.userRequest.LoginRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.userRequest.RegistrationRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.JwtResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.MessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.role.RoleRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.user.UserRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.userDetails.UserDetailsImplement;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.mailSenderService.MailSenderService;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.tokenService.RefreshTokenService;

import com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceInterface.UserServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

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
import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceDetailsImplement implements UserDetailsService, UserServiceInterface {

    @Autowired @Lazy
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private MailSenderService mailSenderService;

    @Value("${tutor-online.app.token.forget.password}")
    private String TOKEN_FORGET_PASS;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User Not Found")
                );
        return UserDetailsImplement.build(user);
    }


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
    public void updateUser(Long id, User user) {
        User userFromDb = userRepository.findById(id).get();
        System.out.println(userFromDb.toString());
        userFromDb.setUsername(user.getUsername());
        userFromDb.setEmail(user.getEmail());
        userFromDb.setFullName(user.getFullName());
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
    public JwtResponse handleUserLogin(LoginRequest loginRequest) throws Exception {
        loadUserByUsername(loginRequest.getUsername());
        boolean isActivated = userRepository.findByUsername(loginRequest.getUsername()).get().getActiveStatus();
        if(!isActivated){
            throw new Exception("User must be activated!");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImplement userDetails  = (UserDetailsImplement) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        return new JwtResponse(
                jwt,
                refreshToken.getToken(),
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles
        );
    }

    @Override
    public MessageResponse handleUserRegistration(RegistrationRequest registrationRequest) throws MessagingException {
        if (userRepository.existsByUsername(registrationRequest.getUsername())) {
            return new MessageResponse("Error: Username is already taken!");
        }
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            return new MessageResponse("Error: Email is already taken!");
        }
        //Create user
        String stringOfToken = UUID.randomUUID().toString();
        registrationRequest.setPassword(encoder.encode(registrationRequest.getPassword()));
        User user = new User(
                registrationRequest.getUsername(),
                registrationRequest.getEmail(),
                registrationRequest.getFullName(),
                registrationRequest.getPassword(),
                stringOfToken
        );
        Set<String> strRoles = registrationRequest.getRole();
        Set<Role> roles = new HashSet<>();

        strRoles.forEach(
                role -> {
                    switch (role) {
                        case "ADMIN":
                            Role adminRole = roleRepository.findByUserRole(ERole.ADMIN).get();
                            roles.add(adminRole);
                            break;
                        case "SUPER_ADMIN":
                            Role super_adminRole = roleRepository.findByUserRole(ERole.SUPER_ADMIN).get();
                            roles.add(super_adminRole);
                            break;
                        case "TUTOR":
                            Role tutorRole = roleRepository.findByUserRole(ERole.TUTOR).get();
                            roles.add(tutorRole);
                            break;
                        case "STUDENT":
                            Role studentRole = roleRepository.findByUserRole(ERole.STUDENT).get();
                            roles.add(studentRole);
                            break;
                    }
                }
        );
        user.setRoles(roles);
        userRepository.save(user);
        mailSenderService.sendEmailActivate(user.getUsername(), stringOfToken, user.getEmail());
        return new MessageResponse("User registered successfully!");
    }

    @Override
    public void activeAccount(String activateToken){
        User user = userRepository.findByActivateToken(activateToken).get();
        user.setActivateToken(null);
        user.setActiveStatus(true);
        userRepository.save(user);
    }

    @Override
    public void sendTokenForgetPassword(String email) throws MessagingException{
        User user = userRepository.findByEmail(email).get();
        if(user == null){
            throw new NoSuchElementException("Email not found");
        }
        Long resetCode = 100000+(long)(Math.random()*(999999-100000));
        user.setResetPasswordCode(resetCode);
        userRepository.save(user);
        mailSenderService.sendEmailResetPassword(user.getUsername(), resetCode, user.getEmail());
    }

    @Override
    public User verifiedResetCode(Long resetCode) {
        User user = userRepository.findByResetPasswordCode(resetCode).get();
        if(user == null){
            throw new NoSuchElementException("Reset code not accepted");
        }
        return user;
    }

    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest){
        User user = verifiedResetCode(resetPasswordRequest.getResetCode());
        String newPassword = encoder.encode(resetPasswordRequest.getNewPassword());
        user.setPassword(newPassword);
        user.setResetPasswordCode(null);
        userRepository.save(user);
    }
}
