package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.courseRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class TimeTableRequest {
    private Integer day;
    @JsonProperty(required = false)
    private String startTime;
    @JsonProperty(required = false)
    private String endTime;

}
