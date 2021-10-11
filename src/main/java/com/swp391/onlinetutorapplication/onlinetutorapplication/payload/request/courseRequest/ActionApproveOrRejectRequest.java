package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.courseRequest;

import lombok.*;
import lombok.AllArgsConstructor;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActionApproveOrRejectRequest {
    private Boolean action;
}
