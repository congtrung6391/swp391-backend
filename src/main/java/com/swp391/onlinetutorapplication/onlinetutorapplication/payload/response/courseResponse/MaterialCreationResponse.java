package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaterialCreationResponse {
    private String title;
    private String description;
    private String fileAttach;
    private String linkSharing;
    private Boolean status ;
}
