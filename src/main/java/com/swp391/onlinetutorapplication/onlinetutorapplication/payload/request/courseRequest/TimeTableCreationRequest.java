package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.courseRequest;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class TimeTableCreationRequest {

    private Integer day;

    private LocalTime startTime;

    private LocalTime endTime;
}
