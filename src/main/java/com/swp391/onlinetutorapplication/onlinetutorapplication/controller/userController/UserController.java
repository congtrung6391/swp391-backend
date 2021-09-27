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
@RequestMapping("/api/user")

public class UserController {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private UserServiceInterface userService;

    @GetMapping("/users")
    public String getAllUser(Model model){
        List<User> listUsers = userService.getAllUser();
        model.addAttribute("listUsers", listUsers);
        return "users";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes ra) {
        userService.saveUser(user);
        ra.addAttribute("message", "The user has been save successfully.");
        return "redirect:/users";
    }

    @GetMapping("/users/edit/{username}")
    public String showEditForm(@PathVariable("username") String username, Model model){
        User user = userService.getUser(username);
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Edit User");
        return "user_form";
    }

}
