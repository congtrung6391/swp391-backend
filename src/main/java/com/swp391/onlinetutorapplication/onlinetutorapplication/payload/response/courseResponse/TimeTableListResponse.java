package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.CourseTimetable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeTableListResponse {
    private Boolean status = true;
    List<TimeTableInformation> timeTableList;

    public TimeTableListResponse(List<TimeTableInformation> timeTableList){
        this.timeTableList = timeTableList;
    }
}
