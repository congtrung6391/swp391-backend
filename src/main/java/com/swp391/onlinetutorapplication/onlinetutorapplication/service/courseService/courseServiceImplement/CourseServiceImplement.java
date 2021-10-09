package com.swp391.onlinetutorapplication.onlinetutorapplication.service.courseService.courseServiceImplement;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Course;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Subject;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.ERole;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.Role;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.courseRequest.CourseCreationRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.courseRequest.CourseUpdateRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse.CourseInformationResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.course.CourseRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.course.SubjectRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.user.UserRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.courseService.courseServiceInterface.CourseServiceInterface;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceInterface.UserServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@Slf4j
public class CourseServiceImplement implements CourseServiceInterface {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceInterface userService;

    @Autowired
    SubjectRepository subjectRepository;

    @Override
    public Course handleCourseCreate(CourseCreationRequest courseCreationRequest, String accessToken) {
        accessToken = accessToken.replaceAll("Bearer ","");
        User tutor = userRepository.findByAuthorizationToken(accessToken)
                .orElseThrow(()-> {
                    throw new NoSuchElementException("Not found user");
                });
        if(tutor.getExpireAuthorization().isBefore(Instant.now())){
            userService.handleUserLogout(accessToken);
        }
        Subject subject = subjectRepository.findById(courseCreationRequest.getSubjectId())
                .orElseThrow(()-> {
                    throw new NoSuchElementException("Not found subject");
                });
        Course course = new Course();
        course.setCourseName(courseCreationRequest.getCourseName());
        course.setCourseDescription(courseCreationRequest.getCourseDescription());
        course.setCost(courseCreationRequest.getCost());
        course.setGrade(courseCreationRequest.getGrade());
        course.setLength(courseCreationRequest.getLength());
        course.setTutor(tutor);
        course.setSubject(subject);

        courseRepository.save(course);
        return course;
    }

    @Override
    public List<CourseInformationResponse> getAllCourseInformationForAdmin() {
        List<Course> listAllCourse = courseRepository.findAll();
        if (listAllCourse.isEmpty()) {
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
        if (listAllCourse.isEmpty()) {
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
        accessToken = accessToken.replaceAll("Bearer ", "");
        User student = userRepository.findByAuthorizationToken(accessToken)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("Not found user");
                });
        if (student.getExpireAuthorization().isBefore(Instant.now())) {
            userService.handleUserLogout(accessToken);
        }
        Course course = courseRepository.findByIdAndCourseStatusIsTrue(id)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("Course not found");
                });
        course.setCourseStatus(false);
        course.setStudent(student);
        courseRepository.save(course);
    }
    

    @Override
    public void saveSubject(Subject subject) {
        subjectRepository.save(subject);
    }

    @Override
    public List<Subject> getSubjectList(){
        return subjectRepository.findAll();
    }

    @Override
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id).get();
        course.setStatus(false);
        courseRepository.save(course);
    }

    @Override
    public Course updateCourse(CourseUpdateRequest request, Long courseID, String accessToken) {
        Course course = null;
        try {
            accessToken = accessToken.replaceAll("Bearer ", "");
            User user = userRepository.findByAuthorizationToken(accessToken).get();
            Set<Role> Roles = user.getRoles();
            for (Role role : Roles) {
                switch (role.getUserRole()) {
                    case SUPER_ADMIN:
                    case ADMIN:
                        course = courseRepository.findById(courseID).get();
                        break;
                    case TUTOR:
                        course = courseRepository.findByIdAndTutor(courseID, user).get();
                        break;
                    default:
                        return null;
                }
            }
            if (request.getCourseName() != null) {//missing title or not
                course.setCourseName(request.getCourseName());
            }
            if (request.getCourseDescription() != null) {//missing description or not
                course.setCourseDescription(request.getCourseDescription());
            }
            if (request.getCost() != null) {//missing cost or not
                course.setCost(request.getCost());
            }
            if (request.getGrade() != null) {//missing grade or not
                course.setGrade(request.getGrade());
            }
            if (request.getLength() != null) {//missing length or not
                course.setLength(request.getLength());
            }
            if (request.getSubjectId() != null) {//missing subjectid or not
                Subject subject = subjectRepository.findById(request.getSubjectId()).get();
                course.setSubject(subject);
            }

            courseRepository.save(course);
            return courseRepository.getById(courseID);
        } catch (NoSuchElementException e) {
            log.info(e.getMessage());
            return null;
        }
    }
}
