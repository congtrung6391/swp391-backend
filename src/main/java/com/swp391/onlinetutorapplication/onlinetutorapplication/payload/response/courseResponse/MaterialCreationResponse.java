package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.CourseMaterial;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaterialCreationResponse {
    private Long id;
    private String title;
    private String description;
    private String fileAttach;
    private String linkSharing;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean status ;

    public MaterialCreationResponse(CourseMaterial material){
        this.id = material.getId();
        this.title = material.getTitle();
        this.description = material.getDescription();
        this.fileAttach = material.getFileAttach();
        this.linkSharing = material.getLinkShare();
        this.status = material.getStatus();
    }

    public MaterialCreationResponse(String title, String description, String fileAttach, String linkSharing, Boolean status) {
        this.title = title;
        this.description = description;
        this.fileAttach = fileAttach;
        this.linkSharing = linkSharing;
        this.status = status;
    }
}
