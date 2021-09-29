package com.swp391.onlinetutorapplication.onlinetutorapplication.controller.mainController;

import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.MessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceInterface.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class deleteUserController {
    @Autowired
    private UserServiceInterface userService;

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> verifyResetCode(@PathVariable("id") Long id){
        try{
            userService.deleteUser(id);
            return ResponseEntity.ok().body(new MessageResponse("User id: "+id + " is deleted"));
        }catch (NoSuchElementException ex){
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
    }
}
