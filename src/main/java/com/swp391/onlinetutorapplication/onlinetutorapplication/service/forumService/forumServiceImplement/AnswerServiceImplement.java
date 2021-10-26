package com.swp391.onlinetutorapplication.onlinetutorapplication.service.forumService.forumServiceImplement;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Answer;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Question;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.ERole;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.Role;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.forumRequest.AnswerCreateRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.forumResponse.answerResponse.AnswerInformationResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.forumResponse.answerResponse.AnswerListResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.forum.AnswerRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.forum.QuestionRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.role.RoleRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.user.UserRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.forumService.forumServiceInterface.AnswerServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AnswerServiceImplement implements AnswerServiceInterface {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private RoleRepository roleRepository;

    public AnswerInformationResponse createAnswer(String accessToken, AnswerCreateRequest request, Long questionId) {
        accessToken = accessToken.replaceAll("Bearer ", "");
        User currentUser = userRepository.findByAuthorizationToken(accessToken)
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("User not found");
                });

        Question question = questionRepository.findByIdAndStatusIsTrue(questionId)
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("Question Not Found");
                });
        if (request.getContent() == null) {
            throw new IllegalArgumentException("You can't create answer with no content");
        }
        Answer answer = new Answer();
        answer.setContent(request.getContent());
        answer.setQuestion(question);
        answer.setUser(currentUser);
        if (request.getReplyId() != null) {
            Answer replyAnswer = answerRepository.findByIdAndStatusIsTrue(request.getReplyId())
                    .orElseThrow(() -> {
                        throw new IllegalArgumentException("Reply Answer Not Found");
                    });
            answer.setReplyId(replyAnswer);
        }
        answerRepository.save(answer);
        return new AnswerInformationResponse(answer);
    }

    public AnswerListResponse getAnswerList(Long id, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Question question = questionRepository.findByIdAndStatusIsTrue(id).orElseThrow(() -> {
            throw new IllegalArgumentException("Question not found");
        });
        Page<Answer> answerList = answerRepository.findAllByQuestionAndStatusIsTrue(question, pageable);
        List<AnswerInformationResponse> responseList = new ArrayList<>();

        for (Answer answer : answerList.getContent()) {
            responseList.add(new AnswerInformationResponse(answer));
        }
        AnswerListResponse response = new AnswerListResponse(responseList);
        response.setSize(answerList.getTotalElements());
        return response;
    }

    @Override
    public void deleteAnswer(Long questionId, Long answerId, String accessToken) throws Exception {
        accessToken = accessToken.replaceAll("Bearer ", "");
        User user = userRepository.findByAuthorizationToken(accessToken).orElseThrow(() -> {
            throw new NoSuchElementException("User cannot be found.");
        });
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("Question cannot be found.");
                });
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("Answer cannot be found.");
                });
        Role role = roleRepository.findByUserRole(ERole.SUPER_ADMIN).get();
        Role role2 = roleRepository.findByUserRole(ERole.ADMIN).get();

        if (user == question.getUser() || user.getRoles().contains(role) || user.getRoles().contains(role2)) {

            answer.setStatus(false);
            answerRepository.save(answer);
        } else {
            throw new Exception("You are not allowed to delete this answer.");
        }
    }
}
