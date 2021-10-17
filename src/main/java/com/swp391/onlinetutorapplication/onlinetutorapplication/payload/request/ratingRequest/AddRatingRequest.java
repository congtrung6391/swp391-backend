package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.ratingRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddRatingRequest {
    private String description;
    private double value;
    private long subjectId;
}
