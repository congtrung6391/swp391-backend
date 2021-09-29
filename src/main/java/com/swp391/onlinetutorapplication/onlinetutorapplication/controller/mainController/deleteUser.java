package com.swp391.onlinetutorapplication.onlinetutorapplication.controller.mainController;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.refreshToken.RefreshToken;
import com.swp391.onlinetutorapplication.onlinetutorapplication.exception.refreshTokenException.TokenRefreshException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = "http://localhost:3000/")
@RequestMapping("/api/auth")

public class  deleteSuperAdminController {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private UserServiceInterface userService;



    //localhost:8080/api/auth/success/super-admin/delete/{id}
    @DeleteMapping("/success/super-admin/delete/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")    public ResponseEntity<String>
    deleteUser(@PathVariable("id") long id){
        //delete User from DB
        userService.deleteById(id);
        return new ResponseEntity<String>("User deleted successfully!", HttpStatus.OK);

    }
}
