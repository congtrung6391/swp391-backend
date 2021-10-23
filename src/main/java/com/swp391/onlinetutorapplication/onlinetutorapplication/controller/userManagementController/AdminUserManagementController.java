package com.swp391.onlinetutorapplication.onlinetutorapplication.controller.userManagementController;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.superAdminRequest.ChangeRoleUserRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.responseMessage.ErrorMessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.responseMessage.SuccessfulMessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.userResponse.UserInformationResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.userResponse.UserListResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.ratingService.ratingServiceInterface.RatingServiceInterface;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceInterface.UserManagementInterface;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceInterface.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/admin")

public class AdminUserManagementController {
    @Autowired
    private UserServiceInterface userService;

    @Autowired
    private UserManagementInterface userManagement;
    @Autowired
    private RatingServiceInterface ratingService;

    // localhost:8080/api/admin/user/{username}
    @PutMapping("/user/{username}/change-role")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> changeRoleUser(@PathVariable String username,
                                            @RequestBody ChangeRoleUserRequest role) {
        boolean result = userService.changeRole(username, role.getRole());
        if (result) {
            return ResponseEntity.ok(new SuccessfulMessageResponse("Update Successful"));
        } else {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse("Update Failed"));
        }
    }

    // localhost:8080/api/admin/id
    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        try {
            userManagement.deleteUser(id);
            return ResponseEntity.ok().body(new SuccessfulMessageResponse("User id: " + id + " is deleted"));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse("Delete Failed"));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse("Not allowed"));
        }
    }

    @GetMapping("/get-user-list")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> getAllUser(@RequestParam(required = false) String userId,
                                        @RequestParam(required = false) String name,
                                        @RequestParam(name = "page", required = false) Integer page,
                                        @RequestParam(name = "limit", required = false) Integer limit) {
        try {
            if(page == null || page<1){
                page = 1;
            }
            if(limit == null){
                limit = 20;
            }
            if(userId != null || name !=null){
                List<UserInformationResponse> list = userManagement.adminSearchUser(userId,name,page,limit);
                return ResponseEntity.ok().body(new UserListResponse(list));
            }
            List<UserInformationResponse> listUsers = userManagement.getAllUser(page, limit);
            return ResponseEntity.ok().body(new UserListResponse(listUsers));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
        }
    }

    @GetMapping("/get-user-profile/{username}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> getUser(@PathVariable("username") String username) {
        try {
            User user = userManagement.getUser(username);
            return ResponseEntity.ok().body(user);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
        }
    }

    /*

    // Can be reused

    @PutMapping("/update-user/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> updateUser(@RequestHeader(name = "Authorization") String accessToken, @PathVariable("id") Long id, @RequestBody UpdateProfileRequest updateProfileRequest) {
        try {
            userManagement.updateUser(accessToken, id, updateProfileRequest);
            return ResponseEntity.ok().body(new MessageResponse("User id: "+id + " has been updated."));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
    }

     */


}
