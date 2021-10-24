package com.swp391.onlinetutorapplication.onlinetutorapplication.service.forumService.forumServiceImplement;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Course;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.CourseMaterial;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Answer;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Question;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.ERole;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.Role;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.forumRequest.AnswerUpdateRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.forum.AnswerRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.forum.QuestionRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.role.RoleRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.user.UserRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.forumService.forumServiceInterface.AnswerServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.NoSuchElementException;

public class AnswerServiceImplement implements AnswerServiceInterface {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void deleteAnswer(Long questionId, Long answerId, String accessToken) throws Exception{
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

    @Override
    public Answer updateAnswer(AnswerUpdateRequest request, Long questionId, Long answerId, String accessToken) {

    }
}
