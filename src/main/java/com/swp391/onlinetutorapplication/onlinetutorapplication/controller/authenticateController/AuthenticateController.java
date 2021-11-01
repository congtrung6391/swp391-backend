package com.swp391.onlinetutorapplication.onlinetutorapplication.controller.authenticateController;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.refreshToken.RefreshToken;
import com.swp391.onlinetutorapplication.onlinetutorapplication.exception.refreshTokenException.TokenRefreshException;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.resetPasswordRequest.ResetCodeRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.resetPasswordRequest.ResetPasswordRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.responseMessage.ErrorMessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.responseMessage.SuccessfulMessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.utils.jwtUtils.JWTUtils;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.tokenRequest.RefreshTokenRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.userRequest.LoginRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.userRequest.RegistrationRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.JwtResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.MessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.refreshTokenResponse.RefreshTokenResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceInterface.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.net.URI;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/auth")

public class AuthenticateController {


    @Autowired
    private UserServiceInterface userService;

    @PostMapping("/log-in")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        try {
            JwtResponse jwtResponse = userService.handleUserLogin(loginRequest);
            return ResponseEntity.ok().body(jwtResponse);
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
        }

    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest registrationRequest)
            throws MessagingException {
        MessageResponse messageResponse = userService.handleUserRegistration(registrationRequest);
        if (messageResponse.getMessage().contains("Error")) {
            return ResponseEntity.badRequest().body(messageResponse);
        } else {
            return ResponseEntity.ok(messageResponse);
        }
    }


    @PostMapping("/verify-authorization")
    public ResponseEntity<?> verifyAuthorization(@RequestHeader(name = "Authorization") String accessToken) {
        try {
            userService.verifyAccessToken(accessToken);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(name = "Authorization") String accessToken) {
        userService.handleUserLogout(accessToken);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/activate")
    public ResponseEntity activateUser(@RequestParam(name = "token") String token) {
        userService.activeAccount(token);
        //Bấm vào link là bay về trang chủ liền
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("https://swp391-onlinetutor.herokuapp.com/")).build();
    }

    //Sau khi nhập xong email bấm enter thì chuyển tới trang nhập code thì
    @PostMapping("/send-forgot-password")
    public ResponseEntity<?> sendForgetPassword(@RequestParam(name = "email") String email)
            throws MessagingException {
        try {
            userService.sendTokenForgetPassword(email);
            return ResponseEntity.ok().body(new SuccessfulMessageResponse("Reset code sent to your email"));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
        }
    }

    @GetMapping("/reset-code")
    public ResponseEntity<?> verifyResetCode(@RequestBody ResetCodeRequest resetCodeRequest) {
        try {
            userService.verifiedResetCode(resetCodeRequest.getResetCode());
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse("Reset code is not existed"));
        }
    }

    //Nếu code đúng thì đi tới trang reset password
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        try {
            userService.resetPassword(resetPasswordRequest);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
        }
    }
}
