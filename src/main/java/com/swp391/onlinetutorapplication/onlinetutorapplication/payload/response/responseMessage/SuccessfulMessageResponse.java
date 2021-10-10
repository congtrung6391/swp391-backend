package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.responseMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SuccessfulMessageResponse {
    private String message;
    private Boolean status = true;

    public SuccessfulMessageResponse(String message) {
        this.message = message;
    }
}
