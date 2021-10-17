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

    @NotBlank
    private Integer day;

    @NotBlank
    private LocalTime startTime;

    @NotBlank
    private LocalTime endTime;
}
