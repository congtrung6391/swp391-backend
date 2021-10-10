package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.courseRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaterialCreationRequest implements Serializable {
    private String description;
    private String title;
    private MultipartFile fileAttach;
}
