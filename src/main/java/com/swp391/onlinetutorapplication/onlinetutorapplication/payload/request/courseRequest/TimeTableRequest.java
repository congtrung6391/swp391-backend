package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.courseRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class TimeTableRequest {
    private Integer day;
    private LocalTime startTime;
    private LocalTime endTime;

}
