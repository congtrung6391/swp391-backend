package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.CourseMaterial;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
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

    public MaterialListResponse(List<MaterialCreationResponse> materials, int page, int limit) {
        page = page-1;
        this.totalMaterial = materials.size();
        List<MaterialCreationResponse> responses = new ArrayList<>();
        int index = page*limit;
        int des = materialList.size()<(index+limit) ? materialList.size() : index+limit;

        for(int i = index; i< des ; i++){
            responses.add(materials.get(i));
        }
        this.materialList = responses;
    }
}
