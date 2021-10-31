package com.swp391.onlinetutorapplication.onlinetutorapplication.controller.forumController;

import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.forumResponse.questionResponse.ListQuestionResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.responseMessage.ErrorMessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.forumService.forumServiceInterface.QuestionServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/admin/forum")
@RestController
public class AdminForumController {

    @Autowired
    QuestionServiceInterface questionService;

    @GetMapping("/question")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> getAllQuestionByAdmin(@RequestParam(required = false) String name,
                                                   @RequestParam(required = false) Long subjectId,
                                                   @RequestParam(required = false) Integer page,
                                                   @RequestParam(required = false) Integer limit,
                                                   @RequestParam(required = false) String sortBy) {
        try {
            if (page == null || page < 1) {
                page = 1;
            }
            if (limit == null) {
                limit = 20;
            }

            if (name != null || subjectId != null) {

                ListQuestionResponse responseList;
                if (sortBy != null && sortBy.equals("top-trend")) {
                    responseList = questionService.getListQuestionByNameOrSubjectAndTopTrending(name, subjectId, page, limit);
                } else {
                    responseList = questionService.getListQuestionByNameOrSubject(name, subjectId, page, limit);
                }

                return ResponseEntity.ok().body(responseList);
            }

            if (sortBy != null && sortBy.equals("top-trend")) {
                ListQuestionResponse responseList = questionService.getListQuestionByTopTrending(page, limit);
                return ResponseEntity.ok().body(responseList);
            }

            ListQuestionResponse responseList = questionService.getListQuestion(page, limit);
            return ResponseEntity.ok().body(responseList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(e.getMessage()));
        }
    }
}
