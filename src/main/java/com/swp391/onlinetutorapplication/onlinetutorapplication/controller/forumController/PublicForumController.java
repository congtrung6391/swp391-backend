package com.swp391.onlinetutorapplication.onlinetutorapplication.controller.forumController;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Question;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.forumRequest.AnswerCreateRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.forumResponse.answerResponse.AnswerResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.forumResponse.questionResponse.DetailQuestionResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.forumResponse.questionResponse.ListQuestionResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.responseMessage.ErrorMessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.responseMessage.SuccessfulMessageResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.forumService.forumServiceInterface.AnswerServiceInterface;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.forumService.forumServiceInterface.QuestionServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api/public/forum")
@CrossOrigin(origins = "http://localhost:3000/")
@RestController
public class PublicForumController {

    @Autowired
    private QuestionServiceInterface questionService;

    @Autowired
    private AnswerServiceInterface answerService;

    @GetMapping("/question")
    public ResponseEntity<?> getQuestionList(@RequestParam(required = false) String name,
                                             @RequestParam(required = false) Long subjectId,
                                             @RequestParam(required = false) Integer page,
                                             @RequestParam(required = false) Integer limit) {
        try {
            if (page == null || page < 1) {
                page = 1;
            }
            if (limit == null) {
                limit = 20;
            }
            List<Question> questionList = new ArrayList<>();
            if (name != null || subjectId != null) {
                questionList = questionService.getListQuestionByNameOrSubject(name, subjectId, page, limit);
                return ResponseEntity.ok().body(new ListQuestionResponse(questionList));
            }
            questionList = questionService.getListQuestion(page, limit);
            return ResponseEntity.ok().body(new ListQuestionResponse(questionList));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<?> getDetailsQuestion(@PathVariable(name = "questionId")Long questionId){
        try{
            Question question = questionService.getDetailsQuestion(questionId);
            return ResponseEntity.ok().body(new DetailQuestionResponse(question));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/question/{questionId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN') or hasAuthority('TUTOR') or hasAuthority('STUDENT')")
    public ResponseEntity<?> deleteQuestion(@RequestHeader(name = "Authorization") String accessToken,
            @PathVariable(name = "questionId")Long questionId){
        try{
            questionService.deleteQuestion(accessToken,questionId);
            return ResponseEntity.ok().body(new SuccessfulMessageResponse("Delete question successful"));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/question/{questionId}/answer")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN') or hasAuthority('TUTOR') or hasAuthority('STUDENT')")
    public ResponseEntity<?> createAnswer(@RequestHeader(name = "Authorization") String accessToken,
                                          @RequestBody AnswerCreateRequest request,
                                          @PathVariable (name = "questionId") Long questionId){
        try{
            return ResponseEntity.ok().body(new AnswerResponse(answerService.createAnswer(accessToken, request, questionId)));
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
        }
    }

    @GetMapping("question/{questionId}/answer")
    public ResponseEntity<?> getAnswerList(@PathVariable(name = "questionId") Long id,
                                           @RequestParam(name = "page", required = false) Integer page,
                                           @RequestParam(name = "limit", required = false) Integer limit){
        try {
            if (page == null || page < 1) {
                page = 1;
            }
            if (limit == null) {
                limit = 20;
            }
            return ResponseEntity.ok().body(answerService.getAnswerList(id, page, limit));
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
        }
    }
}
