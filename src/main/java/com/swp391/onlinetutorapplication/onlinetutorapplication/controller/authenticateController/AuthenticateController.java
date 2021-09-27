package com.swp391.onlinetutorapplication.onlinetutorapplication.controller.authenticateController;

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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = "http://localhost:3000/")
@RequestMapping("/api/auth")

public class AuthenticateController {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private UserServiceInterface userService;

    @PostMapping("/log-in")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        try {
            JwtResponse jwtResponse = userService.handleUserLogin(loginRequest);
            return ResponseEntity.ok().body(jwtResponse);
        }catch (UsernameNotFoundException ex){
            return ResponseEntity.badRequest().body("UsernameNotFoundException : "+ex.getMessage());
        }catch (Exception ex){
            return ResponseEntity.badRequest().body("Exception : "+ex.getMessage());
        }

    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest registrationRequest) throws MessagingException {
        MessageResponse messageResponse = userService.handleUserRegistration(registrationRequest);
        if(messageResponse.getMessage().contains("Error")){
            return ResponseEntity.badRequest().body(messageResponse);
        }else {
            return ResponseEntity.ok(messageResponse);
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new RefreshTokenResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh refreshToken is not in database!"));
    }

    @GetMapping("/activate")
    public ResponseEntity activateUser(@RequestParam(name = "token") String token) {
        userService.activeAccount(token);
        return ResponseEntity.ok().build();
    }

    //Sau khi nhập xong email bấm enter thì chuyển tới trang nhập code thì
    @PostMapping("/send-forgot-password")
    public ResponseEntity<?> sendForgetPassword(@RequestParam(name = "email") String email) throws MessagingException{
        try {
            userService.sendTokenForgetPassword(email);
            return ResponseEntity.ok().body("Reset code sent to your email");
        }catch (NoSuchElementException ex){
            return ResponseEntity.badRequest().body("Error 500 "+ex.getMessage());
        }
    }

    @GetMapping("/reset-code")
    public ResponseEntity<?> verifyResetCode(@RequestParam(name = "resetCode") Long resetCode){
        try{
            userService.verifiedResetCode(resetCode);
            return ResponseEntity.ok().body("resetCode :"+ resetCode);
        }catch (NoSuchElementException ex){
            return ResponseEntity.badRequest().body("Reset code is not existed");
        }
    }

    //Nếu code đúng thì đi tới trang reset password
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest){
        try {
            userService.resetPassword(resetPasswordRequest);
            return ResponseEntity.ok().build();
        }catch (NoSuchElementException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
