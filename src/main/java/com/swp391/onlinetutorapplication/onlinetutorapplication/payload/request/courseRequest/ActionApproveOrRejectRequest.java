package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.courseRequest;

import lombok.*;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Getter
@Setter
public class ActionApproveOrRejectRequest {
    private boolean action;

}
