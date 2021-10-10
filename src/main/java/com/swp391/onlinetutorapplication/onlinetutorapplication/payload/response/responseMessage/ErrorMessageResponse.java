package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.responseMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessageResponse {
    private String message;
    private Boolean status = false;

    public ErrorMessageResponse(String message) {
        this.message = message;
    }
}
