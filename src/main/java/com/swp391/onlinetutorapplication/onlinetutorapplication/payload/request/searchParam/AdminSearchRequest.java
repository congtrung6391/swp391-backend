package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.searchParam;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
public class AdminSearchRequest {
    private String id ="";
    private String name = "";
}
