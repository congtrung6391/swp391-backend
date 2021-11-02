package com.swp391.onlinetutorapplication.onlinetutorapplication.service.courseService.courseServiceImplement;

import com.dropbox.core.DbxException;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.*;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.ERole;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.Role;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.courseRequest.*;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse.*;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.course.*;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.role.RoleRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.user.UserRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.courseService.courseServiceInterface.CourseServiceInterface;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.dropboxService.DropboxService;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceInterface.UserServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @Autowired
    private AdminCourseCriteriaRepository adminCourseCriteriaRepository;

    @Autowired
    private PublicCourseCriteriaRepository publicCourseCriteriaRepository;


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
    public CourseListResponse getAllCourseInformationForAdmin(String accessToken,
                                                              AdminCourseSearchCriteria adminCourseSearchCriteria,
                                                              CoursePage coursePage) {
        Page<Course> listAllCourse = null;
        accessToken = accessToken.replaceAll("Bearer ", "");
        User user = userRepository.findByAuthorizationToken(accessToken).get();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            adminCourseSearchCriteria.setRole(role);
        }
        adminCourseSearchCriteria.setUserId(user.getId());
        listAllCourse = adminCourseCriteriaRepository.findWithFilter(coursePage, adminCourseSearchCriteria);

        List<CourseInformationResponse> allCourseApi = new ArrayList<>();
        List<Course> list = listAllCourse.getContent();
        if (!list.isEmpty()) {
            for (Course course : list) {
                CourseInformationResponse response = new CourseInformationResponse(course);
                response.setTutor(course.getTutor());
                if (course.getStudent() != null) {
                    response.setStudent(course.getStudent());
                }
                allCourseApi.add(response);
            }
        }
        CourseListResponse response = new CourseListResponse(allCourseApi);
        response.setTotalCourse(listAllCourse.getTotalElements());
        return response;
    }

    @Override
    public CourseInformationResponse getOneCourseApiAdmin(String accessToken, Long courseId) {
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
//                    else{
//                        throw new IllegalArgumentException("You are not allow to view this course");
//                    }
                    break;
            }
        }

        CourseInformationResponse courseInformationResponse = new CourseInformationResponse(course);
        courseInformationResponse.setTutor(course.getTutor());
        return courseInformationResponse;
    }

    public CourseInformationResponse getOneCourseApiPublic(Long courseId) {
        Course course = courseRepository.findByIdAndPublicStatusIsTrueAndStudentIsNull(courseId)
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
    public CourseListResponse getAllCourseInformationForStudent(PublicCourseSearchCriteria publicCourseSearchCriteria,
                                                                CoursePage coursePage) {
        Page<Course> listAllCourse = publicCourseCriteriaRepository.findWithFilter(coursePage ,publicCourseSearchCriteria);
        List<CourseInformationResponse> allCourseApi = new ArrayList<>();
        List<Course> list = listAllCourse.getContent();
        if (!list.isEmpty()) {
            for (Course course : list) {
                CourseInformationResponse response = new CourseInformationResponse(course);
                response.setTutor(course.getTutor());
                allCourseApi.add(response);
            }
        }
        CourseListResponse response = new CourseListResponse(allCourseApi);
        response.setTotalCourse(listAllCourse.getTotalElements());
        return response;
    }

    @Override //by Nam
    public void handleCourseRegisterByStudent(String accessToken, Long id) {
        accessToken = accessToken.replaceAll("Bearer ", "");
        User student = userRepository.findByAuthorizationToken(accessToken)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("Not found user");
                });
        if (student.getExpireAuthorization().isBefore(Instant.now())) {
            userService.handleUserLogout(accessToken);
        }
        Course course = courseRepository.findByIdAndPublicStatusIsTrue(id)
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
        Course course = courseRepository.findByIdAndStatusIsTrue(id).get();
        course.setStatus(false);
        course.setLearningStatus(false);
        course.setPublicStatus(false);
        courseRepository.save(course);
    }

    @Override
    public void handleCourseRegisterByTutor(String accessToken, Long id, ActionApproveOrRejectRequest request) {
        accessToken = accessToken.replaceAll("Bearer ", "");
        User student = userRepository.findByAuthorizationToken(accessToken)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("User cannot be found.");
                });
        Course course = courseRepository.findByIdAndStudentIsNotNullAndPublicStatusIsTrue(id)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("Course cannot be found.");
                });
        if(course.getLearningStatus()==true){
            throw new IllegalArgumentException("Can not do this action again");
        }
        if (request.isAction() == true) {
            course.setLearningStatus(true);
        } else {
            course.setLearningStatus(false);
            course.setStudent(null);
        }
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
                        course = courseRepository.findByIdAndStatusIsTrue(courseID).get();
                        break;
                    case TUTOR:
                        course = courseRepository.findByIdAndTutorAndLearningStatusIsTrue(courseID, user).get();
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
            if (request.getLearningStatus() != null) {//missing learningStatus or not
                course.setLearningStatus(request.getLearningStatus());
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
        Course course = courseRepository.findByIdAndStatusIsTrue(courseId)
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
        CourseMaterial courseMaterial = courseMaterialRepository.findByIdAndStatusIsTrue(materialId)
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
    public MaterialListResponse getCourseMaterial(Long courseId, String accessToken, Integer page, Integer limit) throws IOException, DbxException {
        accessToken = accessToken.replaceAll("Bearer ", "");
        User currentUser = userRepository.findByAuthorizationToken(accessToken).
                orElseThrow(() -> {
                    throw new NoSuchElementException("Not found user");
                });
        Course course = courseRepository.findByIdAndStatusIsTrue(courseId).
                orElseThrow(() -> {
                    throw new NoSuchElementException("Material not found");
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
                    } else if (course.getLearningStatus() == false) {
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
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<CourseMaterial> materialListPage = courseMaterialRepository.findAllByCourseAndStatusIsTrueOrderByIdDesc(course, pageable);
        List<MaterialCreationResponse> materialList = new ArrayList<>();
        for (CourseMaterial courseMaterial : materialListPage.getContent()) {
            MaterialCreationResponse response = new MaterialCreationResponse(courseMaterial);
            materialList.add(response);
        }
        MaterialListResponse response = new MaterialListResponse(materialList);
        response.setTotalMaterial(materialListPage.getTotalElements());
        return response;
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
        Course course = courseRepository.findByIdAndStatusIsTrue(courseId)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("Course not found");
                });
        CourseMaterial courseMaterial = courseMaterialRepository.findByIdAndStatusIsTrue(materialId)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("Material not found");
                });
        Role superAdmin = roleRepository.findByUserRole(ERole.SUPER_ADMIN).get();

        if (tutor.getRoles().contains(superAdmin)) {
            courseMaterial.setStatus(false);
            courseMaterialRepository.save(courseMaterial);
        } else if (tutor != course.getTutor()) {
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
        Course course = courseRepository.findByIdAndStatusIsTrue(courseId).orElseThrow(() -> {
            throw new NoSuchElementException("Course not found");
        });
        CourseTimetable timetable = courseTimeTableRepository.findByIdAndStatusIsTrue(timetableId).orElseThrow(() -> {
            throw new NoSuchElementException("TimeTable not found");
        });
        Role superAdmin = roleRepository.findByUserRole(ERole.SUPER_ADMIN).get();
        Role admin = roleRepository.findByUserRole(ERole.ADMIN).get();
        if ((tutor.getRoles().contains(superAdmin)) || (tutor.getRoles().contains(admin))) {
            timetable.setStatus(false);
            courseTimeTableRepository.save(timetable);
        } else if (tutor != course.getTutor()) {
            throw new Exception("You are not allowed to delete ");
        } else {
            timetable.setStatus(false);
            courseTimeTableRepository.save(timetable);
        }

    }

    @Override
    public CourseTimetable updateCourseTimeTable(Long timeTableId, Long courseId, TimeTableRequest timeTableRequest) throws Exception {
        Course course = courseRepository.findByIdAndStatusIsTrue(courseId).orElseThrow(() -> {
            throw new NoSuchElementException("Course not found");
        });
        CourseTimetable timetable = courseTimeTableRepository.findByIdAndStatusIsTrue(timeTableId).orElseThrow(() -> {
            throw new NoSuchElementException("Timetable not found");
        });
        if (timeTableRequest.getDay() != null) {
            timetable.setDay(timeTableRequest.getDay());
        }
        if (timeTableRequest.getStartTime() != null) {
            timetable.setStartTime(LocalTime.parse(timeTableRequest.getStartTime(), DateTimeFormatter.ofPattern("HH:mm:ss")));

        }
        if (timeTableRequest.getEndTime() != null) {
            timetable.setEndTime(LocalTime.parse(timeTableRequest.getEndTime(), DateTimeFormatter.ofPattern("HH:mm:ss")));
        }

        // check if starttime after endtime
        if (timetable.getStartTime().isAfter(timetable.getEndTime())) {
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
        Course course = courseRepository.findByIdAndStatusIsTrue(courseId)
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
        Course course = courseRepository.findByIdAndStatusIsTrue(courseId).
                orElseThrow(() -> {
                    throw new NoSuchElementException("Course cannot be found");
                });
        List<CourseTimetable> timetableList = courseTimeTableRepository.findAllByCourseAndStatusIsTrue(course);
        List<TimeTableInformation> timeTableInformations = new ArrayList<>();
        for (CourseTimetable courseTimetable : timetableList) {
            TimeTableInformation response = new TimeTableInformation(courseTimetable);
            timeTableInformations.add(response);
        }
        return timeTableInformations;
    }

    @Override
    public void handleToggleCourseByAdmin(Long courseId){
        Course course = courseRepository.findByIdAndLearningStatusIsFalse(courseId)
                .orElseThrow(()->{
                    throw new NoSuchElementException("Course was public or deleted");
                });

        course.setPublicStatus(true);
        courseRepository.save(course);
    }
}
