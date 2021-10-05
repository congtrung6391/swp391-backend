package com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.userRequest;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateProfileRequest {

    @NotBlank
    @Size(max = 50)
    private String fullName;

    @NotBlank
    @Size(max = 50)
    private String email;

    @Size(max = 15)
    private String phone;
}
