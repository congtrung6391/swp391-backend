package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.CourseTimetable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeTableInformation {
    private Long id;
    private int day;
    private LocalTime startTime;
    private LocalTime endTime;

    public TimeTableInformation(CourseTimetable timetable) {
        this.id = timetable.getId();
        this.day= timetable.getDay();
        this.startTime = timetable.getStartTime();
        this.endTime = timetable.getEndTime();
    }
}