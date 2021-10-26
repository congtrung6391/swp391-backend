package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.resetPasswordRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetCodeRequest {
    private Long resetCode;
}
