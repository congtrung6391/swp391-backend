package com.swp391.onlinetutorapplication.onlinetutorapplication.service.courseService.courseServiceInterface;

import com.dropbox.core.DbxException;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Course;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Subject;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.courseRequest.CourseCreationRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.courseRequest.CourseUpdateRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.courseRequest.MaterialCreationRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse.CourseInformationResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.utils.dropboxUtil.DropboxAction;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface CourseServiceInterface {
    Course handleCourseCreate(CourseCreationRequest courseCreationRequest, String accessToken);
     List<CourseInformationResponse> getAllCourseInformationForAdmin();
    List<CourseInformationResponse> getAllCourseInformationForStudent();
    void handleCourseRegister(String accessToken, Long id);
    void saveSubject(Subject subject);
    List<Subject> getSubjectList();
    Course updateCourse(CourseUpdateRequest request, Long courseID, String accessToken);
    Object uploadMaterial(Long courseId, MaterialCreationRequest request,MultipartFile fileAttach) throws IOException, DbxException;
    Object updateMaterial(Long courseId, Long materialId, MaterialCreationRequest request, MultipartFile file) throws IOException, DbxException;
    List<Map<String, Object>> getCourseMaterial(Long courseId,Long materialId) throws IOException, DbxException;
    Object getShareableLink(Long courseId,String materialId,String fileName);
    void deleteCourse(Long id);
    void deleteMaterial(Long materialId, Long courseId, String accessToken ) throws Exception;
}
