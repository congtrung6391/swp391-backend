package com.swp391.onlinetutorapplication.onlinetutorapplication.controller.userController;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.refreshToken.RefreshToken;
import com.swp391.onlinetutorapplication.onlinetutorapplication.exception.refreshTokenException.TokenRefreshException;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.resetPasswordRequest.ResetPasswordRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.utils.jwtUtils.JWTUtils;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.tokenRequest.RefreshTokenRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.userRequest.LoginRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.userRequest.RegistrationRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.JwtResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.MessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.refreshTokenResponse.RefreshTokenResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceInterface.UserServiceInterface;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.tokenService.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = "http://localhost:3000/")
@RequestMapping("/api/admin")

public class UserController {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private UserServiceInterface userService;

    @GetMapping("/user/get-user-list")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<List<User>> getAllUser(){
        List<User> listUsers = userService.getAllUser();
        return new ResponseEntity<>(listUsers, HttpStatus.OK);
    }

    // still working on
    @GetMapping("/user/get-user-profile")
    public ResponseEntity<User> getUser(@PathVariable("username") String username) {
        User user = userService.getUser(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // still working on
    @PostMapping("/user/update-user")
    public ResponseEntity updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        userService.updateUser(id, user);
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }


}
