package com.swp391.onlinetutorapplication.onlinetutorapplication.service.forumService.forumServiceImplement;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Subject;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Answer;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.forum.Question;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.forumRequest.QuestionRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.forumResponse.questionResponse.DetailQuestionResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.forumResponse.questionResponse.ListQuestionResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.forumResponse.questionResponse.QuestionResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.course.SubjectRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.forum.AnswerRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.forum.QuestionRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.user.UserRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.forumService.forumServiceInterface.QuestionServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class QuestionServiceImplement implements QuestionServiceInterface {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @Override
    public ListQuestionResponse getListQuestion(Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Question> pageQuestion = questionRepository.findAllByStatusIsTrueOrderByIdDesc(pageable);
        List<Question> list = pageQuestion.getContent();
        for (Question question : list) {
            List<Answer> answers = answerRepository.findAllByQuestionAndStatusIsTrue(question);
            question.setAnswer(answers);
        }
        ListQuestionResponse response = new ListQuestionResponse(list);
        response.setTotalQuestion(pageQuestion.getTotalElements());
        return response;
    }

    @Override
    public ListQuestionResponse getListQuestionByNameOrSubject(String name, Long subjectId, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Question> questionPage = null;
        if (name != null && subjectId == null) {
            questionPage = questionRepository.findAllByStatusIsTrueAndTitleContainingOrderByIdDesc(name, pageable);
        } else if (name == null && subjectId != null) {
            Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> {
                throw new NoSuchElementException("Subject not found");
            });
            questionPage = questionRepository.findAllByStatusIsTrueAndSubjectOrderByIdDesc(subject, pageable);
        } else if (name != null && subjectId != null) {
            Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> {
                throw new NoSuchElementException("Subject not found");
            });
            questionPage = questionRepository.findAllByStatusIsTrueAndTitleContainingAndSubjectOrderByIdDesc(name, subject, pageable);
        }
        List<Question> questionList = questionPage.getContent();
        ListQuestionResponse response = new ListQuestionResponse(questionList);
        response.setTotalQuestion(questionPage.getTotalElements());
        return response;
    }

    @Override
    public QuestionResponse getDetailsQuestion(Long questionId) {
        Question question = questionRepository.findByIdAndStatusIsTrue(questionId)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("Question not found");
                });

        DetailQuestionResponse detail = new DetailQuestionResponse(question);
        QuestionResponse response = new QuestionResponse(detail);
        return response;
    }

    @Override
    public void deleteQuestion(String accessToken, Long questionId) {
        accessToken = accessToken.replaceAll("Bearer ", "");
        User user = userRepository.findByAuthorizationToken(accessToken)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("User not found");
                });
        Question question = questionRepository.findByIdAndStatusIsTrue(questionId)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("Question not found");
                });
        ;
        if (user.getRoles().iterator().next().getUserRole().name().equals("STUDENT")) {
            if (question.getUser().getId() != user.getId()) {
                throw new IllegalArgumentException("You can not delete this question");
            }
        } else if (user.getRoles().iterator().next().getUserRole().name().equals("TUTOR")) {
            if (question.getUser().getId() != user.getId()) {
                throw new IllegalArgumentException("You can not delete this question");
            }
        }
        question.setStatus(false);
        questionRepository.save(question);
    }

    @Override
    public Question createQuestion(QuestionRequest questionRequest, String accessToken) {
        accessToken = accessToken.replaceAll("Bearer ", "");
        User user = userRepository.findByAuthorizationToken(accessToken).orElseThrow(() -> {
            throw new NoSuchElementException("User not found");
        });
        Subject subject = subjectRepository.findById(questionRequest.getSubjectId()).orElseThrow(() -> {
            throw new NoSuchElementException("subject not found");
        });
        Question question = new Question();
        question.setTitle(questionRequest.getTitle());
        question.setDescription(questionRequest.getDescription());
        question.setSubject(subject);
        question.setUser(user);
        questionRepository.save(question);
        return question;
    }

    @Override
    public Question updateQuestion(QuestionRequest questionRequest, String accessToken, Long questionId) throws Exception {
        accessToken = accessToken.replaceAll("Bearer ", "");
        User user = userRepository.findByAuthorizationToken(accessToken).orElseThrow(() -> {
            throw new NoSuchElementException("User not found");
        });
        Question question = questionRepository.findByIdAndStatusIsTrue(questionId).orElseThrow(() -> {
            throw new NoSuchElementException("Question not found");
        });

        if (user != question.getUser()) {
            throw new Exception("You are not allow to update");
        } else {
            if (questionRequest.getTitle() != null) {
                question.setTitle(questionRequest.getTitle());
            }
            if (questionRequest.getDescription() != null) {
                question.setDescription(questionRequest.getDescription());
            }
            if (questionRequest.getSubjectId() != null) {
                Subject subject = subjectRepository.findById(questionRequest.getSubjectId()).orElseThrow(() -> {
                    throw new NoSuchElementException("Subject not found");
                });
                question.setSubject(subject);
            }
        }
        questionRepository.save(question);
        return question;

    }

    @Override
    public ListQuestionResponse getListQuestionByTopTrending(Integer page, Integer limit) {

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Long> questionID = questionRepository.findAllByListAndAnswer(pageable);
        List<Question> questionList = new ArrayList<>();

        for (Long id : questionID.getContent()) {
            Question question = questionRepository.findByIdAndStatusIsTrue(id).get();
            questionList.add(question);
        }

        ListQuestionResponse response = new ListQuestionResponse(questionList);
        response.setTotalQuestion(questionID.getTotalElements());

        return response;
    }

    @Override
    public ListQuestionResponse getListQuestionByNameOrSubjectAndTopTrending(String name, Long subjectId, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Long> questionID = null;
        if (name != null && subjectId == null) {

            questionID = questionRepository.findAllByListAndAnswerAndName("%" + name + "%", pageable);

        } else if (name == null && subjectId != null) {

            Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> {
                throw new NoSuchElementException("Subject not found");
            });
            questionID = questionRepository.findAllByListAndAnswerAndSubject(subject, pageable);

        } else if (name != null && subjectId != null) {

            Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> {
                throw new NoSuchElementException("Subject not found");
            });
            questionID = questionRepository.findAllByListAndAnswerAndSubjectAndName(subject, "%" + name + "%", pageable);

        }

        List<Question> questionList = new ArrayList<>();

        for (Long id : questionID.getContent()) {
            Question question = questionRepository.findByIdAndStatusIsTrue(id).get();
            questionList.add(question);
        }

        ListQuestionResponse response = new ListQuestionResponse(questionList);
        response.setTotalQuestion(questionID.getTotalElements());

        return response;
    }

}
