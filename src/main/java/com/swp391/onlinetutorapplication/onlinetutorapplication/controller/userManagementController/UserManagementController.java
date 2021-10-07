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

    // localhost:8080/api/admin/user/{username}
    @PostMapping("/user/{username}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> changeRoleUser(@PathVariable String username, @RequestBody ChangeRoleUserRequest role){
        boolean result = userService.changeRole(username, role.getRole());
        if(result){
            return ResponseEntity.ok(new StatusResponse("Update Successful", "truegit"));
        }else{
            return ResponseEntity.badRequest().body(new StatusResponse("Update Failed", "false"));
        }
    }

    // localhost:8080/api/admin/id
    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        try{
            userManagement.deleteUser(id);
            return ResponseEntity.ok().body(new StatusResponse("User id: "+id + " is deleted", "true"));
        }catch (NoSuchElementException ex){
            return ResponseEntity.badRequest().body(new StatusResponse("Delete Failed", "false"));
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(new StatusResponse("Not allowed", "false"));
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

    @PostMapping("/update-user/{id}")
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
