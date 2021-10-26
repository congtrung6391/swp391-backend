package com.swp391.onlinetutorapplication.onlinetutorapplication.service.courseService.courseServiceImplement;

import com.dropbox.core.DbxException;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Course;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.CourseMaterial;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.CourseTimetable;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Subject;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.ERole;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.Role;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.courseRequest.*;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse.CourseInformationResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse.MaterialCreationResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse.TimeTableInformation;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.course.CourseMaterialRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.course.CourseRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.course.CourseTimeTableRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.course.SubjectRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.role.RoleRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.user.UserRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.courseService.courseServiceInterface.CourseServiceInterface;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.dropboxService.DropboxService;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceInterface.UserServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    private DropboxService dropboxService;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private CourseMaterialRepository courseMaterialRepository;

    @Autowired
    private CourseTimeTableRepository courseTimeTableRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public Course handleCourseCreate(CourseCreationRequest courseCreationRequest, String accessToken) {
        accessToken = accessToken.replaceAll("Bearer ", "");
        User tutor = userRepository.findByAuthorizationToken(accessToken)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("Not found user");
                });
        if (tutor.getExpireAuthorization().isBefore(Instant.now())) {
            userService.handleUserLogout(accessToken);
        }
        Subject subject = subjectRepository.findById(courseCreationRequest.getSubjectId())
                .orElseThrow(() -> {
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
    public List<CourseInformationResponse> getAllCourseInformationForAdmin(String accessToken,
                                                                           Integer page,
                                                                           Integer limit,
                                                                           Long id,
                                                                           String courseName,
                                                                           Long subjectId,
                                                                           String fullName) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        List<Course> listAllCourse = null;
        accessToken = accessToken.replaceAll("Bearer ", "");
        User user = userRepository.findByAuthorizationToken(accessToken).get();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            switch (role.getUserRole()) {
                case SUPER_ADMIN:
                case ADMIN:
//                    listAllCourse = courseRepository.findAllByStatusIsTrueOrderByIdDesc(id, courseName, subjectId, fullName, pageable);
                    listAllCourse = courseRepository.findAllByStatusIsTrueOrderByIdDesc(pageable);
                    break;
                case TUTOR:
                    listAllCourse = courseRepository.findAllByTutorAndStatusIsTrueOrderByIdDesc(user, pageable);
                    break;
                case STUDENT:
                    listAllCourse = courseRepository.findAllByStudentAndStatusIsTrueOrderByIdDesc(user, pageable);
                    break;
                default:
                    return null;
            }
        }

        List<CourseInformationResponse> allCourseApi = new ArrayList<>();
        if (!listAllCourse.isEmpty()) {
            for (Course course : listAllCourse) {
                CourseInformationResponse response = new CourseInformationResponse(course);
                response.setTutor(course.getTutor());
                if (course.getStudent() != null) {
                    response.setStudent(course.getStudent());
                }
                allCourseApi.add(response);
            }
        }
        return allCourseApi;
    }

    @Override
    public CourseInformationResponse getOneCourseApi(String accessToken, Long courseId) {
        Course course = courseRepository.findByIdAndStatusIsTrue(courseId)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("Course not found");
                });

        accessToken = accessToken.replaceAll("Bearer ", "");
        User currentUser = userRepository.findByAuthorizationToken(accessToken)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("User is not authorized");
                });
        Set<Role> roles = currentUser.getRoles();
        for (Role role : roles) {
            switch (role.getUserRole()) {
                case ADMIN:
                case SUPER_ADMIN:
                    break;
                case TUTOR:
//                        if(course.getTutor().getId())
                    if (course.getTutor().getId() != currentUser.getId()) {
                        if (course.getStudent() != null) {
                            throw new IllegalArgumentException("You are not allow to view this course");
                        }
                    }
                    break;
                case STUDENT:
                    if (course.getStudent() != null) {
                        if (course.getStudent().getId() != currentUser.getId()) {
                            throw new IllegalArgumentException("You are not allow to view this course");
                        }
                    }
                    break;
            }
        }

        CourseInformationResponse courseInformationResponse = new CourseInformationResponse(course);
        courseInformationResponse.setTutor(course.getTutor());
        return courseInformationResponse;
    }

    public CourseInformationResponse getOneCourseApiPublic(Long courseId) {
        Course course = courseRepository.findByIdAndStatusIsTrue(courseId)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("Course not found");
                });
        if (course.getStudent() != null) {
            throw new IllegalArgumentException("You are not allowed to see this content");
        }
        CourseInformationResponse courseInformationResponse = new CourseInformationResponse(course);
        courseInformationResponse.setTutor(course.getTutor());
        return courseInformationResponse;
    }


    @Override
    public List<CourseInformationResponse> getAllCourseInformationForStudent(Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        List<Course> listAllCourse = courseRepository.findAllByStudentIsNullAndCourseStatusIsTrueAndStatusIsTrueOrderByIdDesc(pageable);

        List<CourseInformationResponse> allCourseApi = new ArrayList<>();
        if (!listAllCourse.isEmpty()) {
            for (Course course : listAllCourse) {
                CourseInformationResponse response = new CourseInformationResponse(course);
                response.setTutor(course.getTutor());
                allCourseApi.add(response);
            }
        }
        return allCourseApi;
    }

    @Override //by Nam
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
        if (course.getStudent() != null) {
            throw new NoSuchElementException("Course not available now");
        }
        course.setStudent(student);
        courseRepository.save(course);
    }

    @Override
    public void saveSubject(Subject subject) {
        subjectRepository.save(subject);
    }

    @Override
    public List<Subject> getSubjectList() {
        return subjectRepository.findAll();
    }

    @Override
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id).get();
        course.setStatus(false);
        courseRepository.save(course);
    }

    @Override
    public void handleCourseRegisterRequest(String accessToken, Long id, ActionApproveOrRejectRequest request) {
        accessToken = accessToken.replaceAll("Bearer ", "");
        User student = userRepository.findByAuthorizationToken(accessToken)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("User cannot be found.");
                });
        Course course = courseRepository.findByIdAndStudentIsNotNullAndCourseStatusIsTrue(id)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("Course cannot be found.");
                });
        if (request.isAction() == true) {
            course.setCourseStatus(false);
            courseRepository.save(course);
        } else {
            course.setStudent(null);
            courseRepository.save(course);
        }
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
            if (request.getCourse_status() != null) {//missing courseStatus or not
                course.setCourseStatus(request.getCourse_status());
            }

            courseRepository.save(course);
            return courseRepository.getById(courseID);
        } catch (NoSuchElementException e) {
            log.info(e.getMessage());
            return null;
        }
    }

    @Override
    public Object uploadMaterial(Long courseId, MaterialCreationRequest request) throws IOException, DbxException {
        if (request.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title not null");
        }
        CourseMaterial courseMaterial = null;
        if (request.getFileAttach() == null) {
            courseMaterial = new CourseMaterial(request.getDescription(), request.getTitle());
        } else {
            courseMaterial = new CourseMaterial(request.getDescription(), request.getTitle(), request.getFileAttach().getOriginalFilename());
        }
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("Course not found");
                });
        courseMaterial.setCourse(course);
        courseRepository.save(course);
        courseMaterialRepository.save(courseMaterial);
        //file được phép null
        if (request.getFileAttach() == null) {
            dropboxService.createFolder(Long.toString(courseId), Long.toString(courseMaterialRepository.count()));
            return new MaterialCreationResponse(courseMaterialRepository.count(), courseMaterial.getTitle(),
                    courseMaterial.getDescription(), null, null, true);
        }
        return dropboxService.uploadFile(request.getFileAttach(), Long.toString(courseId), Long.toString(courseMaterialRepository.count()));
    }

    @Override
    public Object updateMaterial(Long courseId, Long materialId, MaterialCreationRequest request) throws IOException, DbxException {
        if (request.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title not null");
        }
        CourseMaterial courseMaterial = courseMaterialRepository.findById(materialId)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("Course material not found");
                });
        //Nếu giá trị nhập vào trống thì lấy lại giá trị cũ
        if (request.getTitle() != null) {
            courseMaterial.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            courseMaterial.setDescription(request.getDescription());
        }
        if (request.getFileAttach() != null) {
            courseMaterial.setFileAttach(request.getFileAttach().getOriginalFilename());
        }
        //nếu 3 cái trống hết thì ném lỗi
        if (request.getTitle() == null && request.getDescription() == null && request.getFileAttach() == null)
            throw new IllegalArgumentException("All field must be fill");
        courseMaterialRepository.save(courseMaterial);

        //nếu file trống thì xóa file ở database
        if (request.getFileAttach() == null) {
            return new MaterialCreationResponse(courseMaterial.getId(), courseMaterial.getTitle(),
                    courseMaterial.getDescription(), courseMaterial.getFileAttach(), courseMaterial.getLinkShare(), true);
        }
        return dropboxService.uploadOverwrittenFile(request.getFileAttach(), Long.toString(courseId), Long.toString(materialId));
    }


    //Ai có task get material thì sửa lại api response của list materials - nam
    @Override
    public List<MaterialCreationResponse> getCourseMaterial(Long courseId, String accessToken) throws IOException, DbxException {
        accessToken = accessToken.replaceAll("Bearer ", "");
        User currentUser = userRepository.findByAuthorizationToken(accessToken).
                orElseThrow(() -> {
                    throw new NoSuchElementException("Not found user");
                });
        Course course = courseRepository.findByIdAndStatusIsTrue(courseId).
                orElseThrow(() -> {
                    throw new NoSuchElementException("Material unauthorizated");
                });

        Set<Role> roles = currentUser.getRoles();
        for (Role role : roles) {
            switch (role.getUserRole()) {
                case SUPER_ADMIN:
                case ADMIN:
                    break;
                case TUTOR:
                    if (currentUser.getId() != course.getTutor().getId()) {
                        throw new IllegalArgumentException("You are not allow to see other material");
                    }
                    break;
                case STUDENT:
                    if (course.getStudent() == null) {
                        throw new IllegalArgumentException("You are not allow to see other material");
                    } else if (course.getCourseStatus() == true) {
                        throw new IllegalArgumentException("You are not allow to see the material");
                    }
                    if (currentUser.getId() != course.getStudent().getId()) {
                        throw new IllegalArgumentException("You are not allow to see other material");
                    }
                    break;
                default:
                    throw new NoSuchElementException("Material not found");
            }
        }
        List<CourseMaterial> materialList = courseMaterialRepository.findAllByCourseAndStatusIsTrue(course);
        List<MaterialCreationResponse> materialCreationResponses = new ArrayList<>();
        for (CourseMaterial courseMaterial : materialList) {
            MaterialCreationResponse response = new MaterialCreationResponse(courseMaterial);
            materialCreationResponses.add(response);
        }
        return materialCreationResponses;
    }

    //Link share đã lưu vào database, nên không cần dùng hàm này cũng được, sài getLinkShare là lấy đc link rồi - nam
    @Override
    public Object getShareableLink(Long courseId, String materialId, String fileName) {
        return dropboxService.getShareLink(Long.toString(courseId), materialId, fileName);
    }

    @Override
    public void deleteMaterial(Long materialId, Long courseId, String accessToken) throws Exception {
        accessToken = accessToken.replaceAll("Bearer ", "");
        // check existed
        User tutor = userRepository.findByAuthorizationToken(accessToken).orElseThrow(() -> {
            throw new NoSuchElementException("User not found");
        });
        Course course = courseRepository.findByIdAndCourseStatusIsTrue(courseId)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("Course not found");
                });
        CourseMaterial courseMaterial = courseMaterialRepository.findById(materialId)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("Material not found");
                });

        if (tutor != course.getTutor()) {
            throw new Exception("You are not allowed to delete");
        } else {
            courseMaterial.setStatus(false);
            courseMaterialRepository.save(courseMaterial);
        }


    }

    //Delete TimeTable
    @Override
    public void deleteTimeTable(Long timetableId, Long courseId, String accessToken) throws Exception {
        accessToken = accessToken.replaceAll("Bearer ", "");
        User tutor = userRepository.findByAuthorizationToken(accessToken).orElseThrow(() -> {
            throw new NoSuchElementException("User not found");
        });
        Course course = courseRepository.findByIdAndCourseStatusIsTrue(courseId).orElseThrow(() -> {
            throw new NoSuchElementException("Course not found");
        });
        CourseTimetable timetable = courseTimeTableRepository.findById(timetableId).orElseThrow(() -> {
            throw new NoSuchElementException("TimeTable not found");
        });
        Role SuperAmin = roleRepository.findByUserRole(ERole.SUPER_ADMIN).get();

        if (tutor.getRoles().contains(SuperAmin)) {
            timetable.setStatus(false);
            courseTimeTableRepository.save(timetable);
        }
        else if(tutor != course.getTutor() ) {
            throw new Exception("You are not allowed to delete ");
        }else {
            timetable.setStatus(false);
            courseTimeTableRepository.save(timetable);
        }

    }

    @Override
    public CourseTimetable updateCourseTimeTable(Long timeTableId, Long courseId, TimeTableRequest timeTableRequest) throws Exception {
        Course course = courseRepository.findByIdAndCourseStatusIsTrue(courseId).orElseThrow(() -> {
            throw new NoSuchElementException("Course not found");
        });
        CourseTimetable timetable = courseTimeTableRepository.findById(timeTableId).orElseThrow(() -> {
            throw new NoSuchElementException("Timetable not found");
        });
        if (timeTableRequest.getDay() != null) {
            timetable.setDay(timeTableRequest.getDay());
        }
        if(timeTableRequest.getStartTime() != null){
            timetable.setStartTime(LocalTime.parse(timeTableRequest.getStartTime(), DateTimeFormatter.ofPattern("HH:mm:ss")));

        }
        if(timeTableRequest.getEndTime() != null){
            timetable.setEndTime(LocalTime.parse(timeTableRequest.getEndTime(), DateTimeFormatter.ofPattern("HH:mm:ss")));
        }

        // check if starttime after endtime
        if(timetable.getStartTime().isAfter(timetable.getEndTime())){
          throw new Exception("Please set Startime before EndTime");
        }
        courseTimeTableRepository.save(timetable);
        return timetable;
    }

    @Override
    public CourseTimetable createTimetable(TimeTableCreationRequest request, Long courseId, String accessToken) throws Exception {
        accessToken = accessToken.replaceAll("Bearer ", "");
        User tutor = userRepository.findByAuthorizationToken(accessToken)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("User cannot be found");
                });
        if (tutor.getExpireAuthorization().isBefore(Instant.now())) {
            userService.handleUserLogout(accessToken);
        }
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("Course cannot be found");
                });
        CourseTimetable timetable = new CourseTimetable();
        timetable.setDay(request.getDay());
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new Exception("Please set StartTime before EndTime");
        }
        timetable.setStartTime(request.getStartTime());
        timetable.setEndTime(request.getEndTime());
        timetable.setCourse(course);

        courseTimeTableRepository.save(timetable);
        return timetable;
    }

    @Override
    public List<TimeTableInformation> getTimeTableList(Long courseId) throws Exception {
        Course course = courseRepository.findById(courseId).
                orElseThrow(() -> {
                    throw new NoSuchElementException("Course cannot be found");
                });
        if (course.getStudent() != null) {
            throw new Exception("Course cannot be found");
        }
        List<CourseTimetable> timetableList = courseTimeTableRepository.findAllByCourseAndStatusIsTrue(course);
        List<TimeTableInformation> timeTableInformations = new ArrayList<>();
        for (CourseTimetable courseTimetable : timetableList) {
            TimeTableInformation response = new TimeTableInformation(courseTimetable);
            timeTableInformations.add(response);
        }
        return timeTableInformations;
    }

}
