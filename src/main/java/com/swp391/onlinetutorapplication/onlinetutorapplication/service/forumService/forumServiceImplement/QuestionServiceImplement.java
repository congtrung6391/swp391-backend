package com.swp391.onlinetutorapplication.onlinetutorapplication.service.forumService.forumServiceImplement;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Subject;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Question;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.ERole;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.Role;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.forumRequest.QuestionRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.course.SubjectRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.forum.QuestionRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.user.UserRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.forumService.forumServiceInterface.QuestionServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@Slf4j
public class QuestionServiceImplement implements QuestionServiceInterface {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Question> getListQuestion(Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        List<Question> list = questionRepository.findAllByStatusIsTrueOrderByIdDesc(pageable);
        return list;
    }

    @Override
    public List<Question> getListQuestionByNameOrSubject(String name, Long subjectId, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        List<Question> questionList = new ArrayList<>();
        if (name != null && subjectId == null) {
            questionList = questionRepository.findAllByStatusIsTrueAndTitleContainingOrderByIdDesc(name, pageable);
        } else if (name == null && subjectId != null) {
            Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> {
                        throw new NoSuchElementException("Subject not found");
                    });
            questionList = questionRepository.findAllByStatusIsTrueAndSubjectOrderByIdDesc(subject,pageable);
        } else if (name != null && subjectId != null) {
            Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> {
                throw new NoSuchElementException("Subject not found");
            });
            questionList = questionRepository.findAllByStatusIsTrueAndTitleContainingAndSubjectOrderByIdDesc(name,subject,pageable);
        }
        return questionList;
    }

    @Override
    public Question getDetailsQuestion(Long questionId) {
        Question question = questionRepository.findByIdAndStatusIsTrue(questionId)
                .orElseThrow(()->{
                   throw new NoSuchElementException("Question not found");
                });
        return question;
    }

    @Override
    public void deleteQuestion(String accessToken, Long questionId) {
        accessToken = accessToken.replaceAll("Bearer ","");
        User user = userRepository.findByAuthorizationToken(accessToken)
                .orElseThrow(()->{
                    throw new NoSuchElementException("User not found");
                });
        Question question = questionRepository.findByIdAndStatusIsTrue(questionId)
                .orElseThrow(()->{
                    throw new NoSuchElementException("Question not found");
                });;
        if(user.getRoles().iterator().next().getUserRole().name().equals("STUDENT")){
            if(question.getUser().getId() != user.getId()){
                throw new IllegalArgumentException("You can not delete this question");
            }
        }else if(user.getRoles().iterator().next().getUserRole().name().equals("TUTOR")) {
            if (question.getUser().getId() != user.getId()) {
                throw new IllegalArgumentException("You can not delete this question");
            }
        }
        question.setStatus(false);
        questionRepository.save(question);
    }

    public Question createQuestion(QuestionRequest questionRequest,String accessToken) {
        accessToken = accessToken.replaceAll("Bearer ","");
        User user = userRepository.findByAuthorizationToken(accessToken).orElseThrow(()->{
            throw new NoSuchElementException("User not found");
        });
        Subject subject = subjectRepository.findById(questionRequest.getSubjectId()).orElseThrow(()->{
            throw new NoSuchElementException("subject not found");
        });
        Question question = new Question();
        question.setTitle(questionRequest.getTitle());
        question.setDescription(questionRequest.getDescription());
        question.setSubject(subject);
        question.setUser(user);
        return question;
    }
}
