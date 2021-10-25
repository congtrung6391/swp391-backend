package com.swp391.onlinetutorapplication.onlinetutorapplication.service.forumService.forumServiceImplement;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Answer;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Question;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.forumRequest.AnswerCreateRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.forumResponse.answerResponse.AnswerInformationResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.forumResponse.answerResponse.AnswerListResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.forum.AnswerRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.forum.QuestionRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.user.UserRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.forumService.forumServiceInterface.AnswerServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnswerServiceImplement implements AnswerServiceInterface {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public AnswerInformationResponse createAnswer(String accessToken, AnswerCreateRequest request, Long questionId) {
        accessToken = accessToken.replaceAll("Bearer ", "");
        User currentUser = userRepository.findByAuthorizationToken(accessToken)
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("User not found");
                });

        Question question = questionRepository.findByIdAndStatusIsTrue(questionId)
                .orElseThrow(() ->{
                    throw new IllegalArgumentException("Question Not Found");
                });
        if(request.getContent() == null){
            throw new IllegalArgumentException("You can't create answer with no content");
        }
        Answer answer = new Answer();
        answer.setContent(request.getContent());
        answer.setQuestion(question);
        answer.setUser(currentUser);
        if(request.getReplyId() != null){
            Answer replyAnswer = answerRepository.findByIdAndStatusIsTrue(request.getReplyId())
                    .orElseThrow(() -> {
                    throw new IllegalArgumentException("Reply Answer Not Found");
            });
            answer.setReplyId(replyAnswer);
        }
        answerRepository.save(answer);
        return new AnswerInformationResponse(answer);
    }

    public AnswerListResponse getAnswerList(Long id, Integer page, Integer limit){
        Pageable pageable = PageRequest.of(page - 1, limit);
        Question question = questionRepository.findByIdAndStatusIsTrue(id).orElseThrow(() -> {
            throw new IllegalArgumentException("Question not found");
        });
        List<Answer> answerList = answerRepository.findAllByQuestionAndStatusIsTrue(question, pageable).orElseThrow(() ->{
            throw new IllegalArgumentException("Answer list not found");
        });
        int size = answerRepository.findAllByQuestionAndStatusIsTrue(question).size();
        List<AnswerInformationResponse> responseList = new ArrayList<>();
        if(size != 0){
            for (Answer answer : answerList) {
                responseList.add(new AnswerInformationResponse(answer));
            }
        }
        return new AnswerListResponse(size, responseList);
    }
}
