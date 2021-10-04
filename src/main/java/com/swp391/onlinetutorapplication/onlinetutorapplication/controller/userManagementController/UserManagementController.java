package com.swp391.onlinetutorapplication.onlinetutorapplication.controller.userManagementController;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.superAdminRequest.ChangeRoleUserRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.userRequest.UpdateProfileRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.MessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.StatusResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceInterface.UserManagementInterface;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceInterface.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/admin")

public class UserManagementController {
    @Autowired
    private UserServiceInterface userService;

    @Autowired
    private UserManagementInterface userManagement;

    @PostMapping("/user/{username}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> changeRoleUser(@PathVariable String username, @RequestBody ChangeRoleUserRequest role){
        boolean result = userService.changeRole(username, role.getRole());
        if(result){
            return ResponseEntity.ok(new StatusResponse("Update Successful", HttpStatus.OK.toString()));
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StatusResponse("Update Failed", HttpStatus.NOT_FOUND.toString()));
        }
    }

    // localhost:8080/api/admin/delete/
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> verifyResetCode(@PathVariable("id") Long id) {
        try{
            userManagement.deleteUser(id);
            return ResponseEntity.ok().body(new MessageResponse("User id: "+id + " is deleted"));
        }catch (NoSuchElementException ex){
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
    }

    @GetMapping("/get-user-list")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> getAllUser(){
        try {
            List<User> listUsers = userManagement.getAllUser();
            return ResponseEntity.ok().body(listUsers);
        }catch(NoSuchElementException ex){
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
    }

    @GetMapping("/get-user-profile/{username}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> getUser(@PathVariable("username") String username) {
        try {
            User user = userManagement.getUser(username);
            return ResponseEntity.ok().body(user);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
    }

    @PutMapping("/update-user/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @RequestBody UpdateProfileRequest updateProfileRequest) {
        try {
            userManagement.updateUser(id, updateProfileRequest);
            return ResponseEntity.ok().body(new MessageResponse("User id: "+id + " has been updated."));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
    }
}
