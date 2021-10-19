package com.swp391.onlinetutorapplication.onlinetutorapplication.controller.userManagementController;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.rating.Rate;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.ratingRequest.AddRatingRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.ratingRequest.UpdateRatingRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.userRequest.UpdateProfileRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.MessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.ratingResponse.RatingListResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.ratingResponse.RatingResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.responseMessage.ErrorMessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.responseMessage.SuccessfulMessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.userResponse.TutorListResponse;
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
@RequestMapping("/api/public")
public class PublicUserManagementController {

    @Autowired
    private UserServiceInterface userService;

    @Autowired
    private UserManagementInterface userManagement;

    @Autowired
    private RatingServiceInterface ratingService;

    @PostMapping("/user/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN') or hasAuthority('ADMIN') or hasAuthority('TUTOR') or hasAuthority('STUDENT')")
    public ResponseEntity<?> updateUser(@RequestHeader(name = "Authorization") String accessToken,
                                        @PathVariable("id") Long id,
                                        @RequestBody UpdateProfileRequest updateProfileRequest) {
        try {
            userManagement.updateUser(accessToken, id, updateProfileRequest);
            return ResponseEntity.ok().body(new MessageResponse("User id: " + id + " has been updated."));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
        }
    }

    @GetMapping("/user/{id}/profile")
    public ResponseEntity<?> responseUserProfile(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok().body(userManagement.getUserProfile(id));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
        }
    }

    @GetMapping("/tutor")
    public ResponseEntity<?> getListTutor(
            @RequestParam(required = false) String name,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "limit", required = false) Integer limit
    ) {
        try {
            if (page == null) {
                page = 1;
            }
            if (limit == null) {
                limit = 20;
            }
            if(name != null){
                List<UserInformationResponse> list = (List<UserInformationResponse>) userManagement.publicSearchUser(name);
                return ResponseEntity.ok().body(new UserListResponse(list,page,limit));
            }
            List<UserInformationResponse> list = userManagement.getListTutor();
            TutorListResponse listResponse = new TutorListResponse(list, page, limit);
            return ResponseEntity.ok().body(listResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @GetMapping("/tutor/{tutorId}/rating")
//    public ResponseEntity<?> getTutorRating(@PathVariable(name = "tutorId") Long tutorId,
//                                            @RequestParam(name = "page", required = false) Integer page,
//                                            @RequestParam(name = "limit", required = false) Integer limit) {
//        try {
//            if (page == null) {
//                page = 1;
//            }
//            if (limit == null) {
//                limit = 20;
//            }
//            List<Rate> rateList = ratingService.getAllRating(tutorId);
//            return ResponseEntity.ok().body(new RatingListResponse(rateList, page, limit));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(new ErrorMessageResponse(e.getMessage()));
//        }
//    }

    @GetMapping("/user/{tutorId}/rating")
    public ResponseEntity<?> getTutorRatingBySubject(@PathVariable(name = "tutorId") Long tutorId,
                                                     @RequestParam(required = false) Long subjectId,
                                                     @RequestParam(name = "page", required = false) Integer page,
                                                     @RequestParam(name = "limit", required = false) Integer limit) {
        try {
            if (page == null) {
                page = 1;
            }
            if (limit == null) {
                limit = 20;
            }
            if(subjectId == null){
                List<Rate> rateList = ratingService.getAllRating(tutorId);
                return ResponseEntity.ok().body(new RatingListResponse(rateList, page, limit));
            }
            List<Rate> rateList = ratingService.getTutorRatingBySubject(tutorId, subjectId);
            return ResponseEntity.ok().body(new RatingListResponse(rateList, page, limit));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(e.getMessage()));
        }
    }

//    @GetMapping("/tutor/search")
//    public ResponseEntity<?> publicSearchTutor(@RequestParam(required = false) String name) {
//        try {
//            return ResponseEntity.ok().body(userManagement.publicSearchUser(name));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

    @PostMapping("/user/{userId}/rating")
    @PreAuthorize("hasAuthority('SUPER_ADMIN') or hasAuthority('STUDENT') or hasAuthority('ADMIN')")
    public ResponseEntity<?> addRating(@RequestHeader(name = "Authorization") String accessToken,
                                       @PathVariable(name = "userId") Long tutorId,
                                       @RequestBody AddRatingRequest request) {
        try {
            return ResponseEntity.ok().body(new RatingResponse(ratingService.addRating(accessToken, tutorId, request)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/user/{userId}/rating/{ratingId}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN') or hasAuthority('STUDENT') or hasAuthority('ADMIN')")
    public ResponseEntity<?> updateRating(@RequestHeader(name = "Authorization") String accessToken,
                                          @PathVariable(name = "userId") Long tutorId,
                                          @PathVariable(name = "ratingId") Long ratingId,
                                          @RequestBody UpdateRatingRequest request) {
        try {
            return ResponseEntity.ok().body(new RatingResponse(ratingService.updateRating(accessToken, tutorId, ratingId, request)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/user/{tutorId}/rating/{ratingId}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN') or hasAuthority('STUDENT') or hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteRating(@RequestHeader(name = "Authorization")String accessToken,
                                          @PathVariable(name = "tutorId") Long tutorId,
                                          @PathVariable(name = "ratingId") Long ratingId){
        try{
            ratingService.deleteRating(accessToken,tutorId,ratingId);
            return ResponseEntity.ok().body(new SuccessfulMessageResponse("Delete rating successful"));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(e.getMessage()));
        }
    }
}
