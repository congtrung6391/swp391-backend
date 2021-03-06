package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.Course;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {
    private CourseInformationResponse course;
    private boolean status = true;

    public CourseResponse(Course course){
        this.course = new CourseInformationResponse(course);
        this.course.setTutor(course.getTutor());
    }
}
