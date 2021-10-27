package com.swp391.onlinetutorapplication.onlinetutorapplication.service.courseService.courseServiceInterface;

import com.dropbox.core.DbxException;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Course;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.CourseTimetable;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Subject;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.courseRequest.*;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse.CourseInformationResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse.MaterialCreationResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse.TimeTableInformation;

import java.awt.print.Pageable;
import java.io.IOException;
import java.util.List;

public interface CourseServiceInterface {
    Course handleCourseCreate(CourseCreationRequest courseCreationRequest, String accessToken);

    //    List<CourseInformationResponse> getAllCourseInformationForAdmin(String accessToken, Integer page, Integer limit, Long id, String courseName, Long subjectId, String fullName);
    List<CourseInformationResponse> getAllCourseInformationForAdmin(String accessToken, Integer page, Integer limit);

    CourseInformationResponse getOneCourseApi(String accessToken, Long courseId);

    List<CourseInformationResponse> getAllCourseInformationForStudent(Integer page, Integer limit);

    void handleCourseRegister(String accessToken, Long id);

    void saveSubject(Subject subject);

    List<Subject> getSubjectList();

    Course updateCourse(CourseUpdateRequest request, Long courseID, String accessToken);

    Object uploadMaterial(Long courseId, MaterialCreationRequest request) throws IOException, DbxException;

    Object updateMaterial(Long courseId, Long materialId, MaterialCreationRequest request) throws IOException, DbxException;

    List<MaterialCreationResponse> getCourseMaterial(Long courseId, String accessToken) throws IOException, DbxException;

    Object getShareableLink(Long courseId, String materialId, String fileName);

    void deleteCourse(Long id);

    void deleteMaterial(Long materialId, Long courseId, String accessToken) throws Exception;

    void handleCourseRegisterRequest(String accessToken, Long id, ActionApproveOrRejectRequest actionApproveOrRejectRequest);

    CourseInformationResponse getOneCourseApiPublic(Long courseId);

    void deleteTimeTable(Long timetableId, Long courseId, String accessToken) throws Exception;

    CourseTimetable updateCourseTimeTable(Long timeTableId, Long courseId, TimeTableRequest timeTableRequest) throws Exception;

    CourseTimetable createTimetable(TimeTableCreationRequest request, Long courseId, String accessToken) throws Exception;

    List<TimeTableInformation> getTimeTableList(Long courseId) throws Exception;
}
