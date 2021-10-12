package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.CourseMaterial;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MaterialListResponse {
    private Boolean status = true;
    private int totalMaterial;
    List<MaterialCreationResponse> materialList;

    public MaterialListResponse(List<MaterialCreationResponse> materialList) {
        this.totalMaterial = materialList.size();
        this.materialList = materialList;
    }
}
