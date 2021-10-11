package com.swp391.onlinetutorapplication.onlinetutorapplication.controller.userManagementController;

import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.userRequest.UpdateProfileRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.MessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.responseMessage.ErrorMessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceInterface.UserManagementInterface;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceInterface.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/public")
public class PublicUserManagementController {

    @Autowired
    private UserServiceInterface userService;

    @Autowired
    private UserManagementInterface userManagement;

    @PostMapping("/user/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN') or hasAuthority('ADMIN') or hasAuthority('TUTOR') or hasAuthority('STUDENT')")
    public ResponseEntity<?> updateUser(@RequestHeader(name = "Authorization") String accessToken,@PathVariable("id") Long id, @RequestBody UpdateProfileRequest updateProfileRequest) {
        try {
            userManagement.updateUser(accessToken,id, updateProfileRequest);
            return ResponseEntity.ok().body(new MessageResponse("User id: "+id + " has been updated."));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
        }
    }

    @GetMapping("/user/{id}/profile")
    public ResponseEntity<?> responseUserProfile(@PathVariable("id") Long id){
        try{
            return ResponseEntity.ok().body(userManagement.getUserProfile(id));
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
        }
    }
}