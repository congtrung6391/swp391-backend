package com.swp391.onlinetutorapplication.onlinetutorapplication.service.courseService.courseServiceImplement;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Course;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.ESubject;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Subject;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.courseRequest.CourseCreationRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse.CourseInformationResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.course.CourseRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.course.SubjectRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.user.UserRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.courseService.courseServiceInterface.CourseServiceInterface;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceInterface.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class CourseServiceImplement implements CourseServiceInterface {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceInterface userService;

    @Override
    public void handleCourseCreate(CourseCreationRequest courseCreationRequest,String accessToken) {
        accessToken = accessToken.replaceAll("Bearer ","");
        User tutor = userRepository.findByAuthorizationToken(accessToken)
                .orElseThrow(()-> {
                    throw new NoSuchElementException("Not found user");
                });
        if(tutor.getExpireAuthorization().isBefore(Instant.now())){
            userService.handleUserLogout(accessToken);
        }
        Course course = new Course();
        course.setCourseName(courseCreationRequest.getCourseName());
        course.setCourseDescription(courseCreationRequest.getCourseDescription());
        course.setCost(courseCreationRequest.getCost());
        course.setGrade(courseCreationRequest.getGrade());
        course.setLength(courseCreationRequest.getLength());
        course.setTutor(tutor);
        switch(courseCreationRequest.getSubject()){
            case "VAT_LY":
                Subject subject = subjectRepository.findBySubjectName(ESubject.VAT_LY).get();
                course.setSubject(subject);
                break;
            case "TOAN":
                 subject = subjectRepository.findBySubjectName(ESubject.TOAN).get();
                course.setSubject(subject);
                break;
            case "HOA_HOC":
                subject = subjectRepository.findBySubjectName(ESubject.HOA_HOC).get();
                course.setSubject(subject);
                break;
            case "SINH_HOC":
                subject = subjectRepository.findBySubjectName(ESubject.SINH_HOC).get();
                course.setSubject(subject);
                break;
            case "TIENG_VIET":
                subject = subjectRepository.findBySubjectName(ESubject.TIENG_VIET).get();
                course.setSubject(subject);
                break;
        }
        courseRepository.save(course);
    }

    @Override
    public List<CourseInformationResponse> getAllCourseInformationForAdmin() {
        List<Course> listAllCourse = courseRepository.findAll();
        if(listAllCourse.isEmpty()){
            throw new NoSuchElementException("Course empty");
        }
        List<CourseInformationResponse> allCourseApi = new ArrayList<>();
        for (Course course : listAllCourse) {
            CourseInformationResponse response = new CourseInformationResponse(
                    course.getId(),
                    course.getCourseName(),
                    course.getCourseDescription(),
                    course.getGrade(),
                    course.getCost(),
                    course.getLength(),
                    course.getCourseStatus(),
                    course.getSubject().getSubjectName().name(),
                    course.getTutor(),
                    course.getTutor().getFullName(),
                    course.getTutor().getPhone(),
                    course.getTutor().getEmail(),
                    course.getStudent()
            );
            allCourseApi.add(response);
        }
        return allCourseApi;
    }

    @Override
    public List<CourseInformationResponse> getAllCourseInformationForStudent() {
        List<Course> listAllCourse = courseRepository.findAllByCourseStatusIsTrue();
        if(listAllCourse.isEmpty()){
            throw new NoSuchElementException("Course empty");
        }
        List<CourseInformationResponse> allCourseApi = new ArrayList<>();
        for (Course course : listAllCourse) {
            CourseInformationResponse response = new CourseInformationResponse(
                    course.getId(),
                    course.getCourseName(),
                    course.getCourseDescription(),
                    course.getGrade(),
                    course.getCost(),
                    course.getLength(),
                    course.getSubject().getSubjectName().name(),
                    course.getTutor().getFullName(),
                    course.getTutor().getPhone(),
                    course.getTutor().getEmail()
            );
            allCourseApi.add(response);
        }
        return allCourseApi;
    }

    @Override
    public void handleCourseRegister(String accessToken, Long id) {
        accessToken = accessToken.replaceAll("Bearer ","");
        User student = userRepository.findByAuthorizationToken(accessToken)
                .orElseThrow(()-> {
                    throw new NoSuchElementException("Not found user");
                });
        if(student.getExpireAuthorization().isBefore(Instant.now())){
            userService.handleUserLogout(accessToken);
        }
        Course course = courseRepository.findByIdAndCourseStatusIsTrue(id)
                .orElseThrow(() ->{
                    throw new NoSuchElementException("Course not found");
                });
        course.setCourseStatus(false);
        course.setStudent(student);
        courseRepository.save(course);
    }

    @Autowired
    SubjectRepository subjectRepository;
    @Override
    public void saveSubject(Subject subject) {
        subjectRepository.save(subject);
    }




}
