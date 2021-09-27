package com.swp391.onlinetutorapplication.onlinetutorapplication.controller.AdminController;

import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.SuperAdminRequest.ChangeRoleUserRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.StatusResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceInterface.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/admin")

public class AdminController {
    @Autowired
    private UserServiceInterface userService;

    @PostMapping("/user/{username}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> changeRoleUser(@PathVariable String username, @RequestBody ChangeRoleUserRequest role){
        boolean result = userService.changeRole(username, role.getRole());
        if(result){
            return ResponseEntity.ok(new StatusResponse("Update Sucess", HttpStatus.OK.toString()));
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StatusResponse("Update Failed", HttpStatus.NOT_FOUND.toString()));
        }
    }
}
