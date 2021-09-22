package com.swp391.onlinetutorapplication.onlinetutorapplication.controller.authenticateController;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.refreshToken.RefreshToken;
import com.swp391.onlinetutorapplication.onlinetutorapplication.exception.refreshTokenException.TokenRefreshException;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.mail.MessagingException;
import javax.validation.Valid;

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
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = userService.handleUserLogin(loginRequest);
        if(jwtResponse.getErrorMessage()!=null){
            //send bad request when login failed
            return ResponseEntity.badRequest().body(jwtResponse);
        }else {
            //send status 200 when successful
            return ResponseEntity.ok(jwtResponse);
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
    public RedirectView activateUser(@RequestParam(name = "token") String token) {
        userService.activeAccount(token);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/api/auth/all");
        return redirectView;
    }

    @GetMapping("/send-forget-password/{email}")
    public ResponseEntity<?> sendForgetPassword(@PathVariable String email) throws MessagingException{
        MessageResponse messageResponse = userService.sendTokenForgetPassword(email);
        if(messageResponse.getMessage().contains("Error")){
            return ResponseEntity.badRequest().body(messageResponse);
        }else{
            return ResponseEntity.ok(messageResponse);
        }
    }


}
