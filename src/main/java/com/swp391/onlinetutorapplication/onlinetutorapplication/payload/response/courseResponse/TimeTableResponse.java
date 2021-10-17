package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.CourseTimetable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeTableResponse {
    private TimeTableInformation timetable;
    private boolean status = true;

    public TimeTableResponse(CourseTimetable timetable){
        this.timetable = new TimeTableInformation(timetable);
    }
}
