package com.swp391.onlinetutorapplication.onlinetutorapplication.model.role;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.ERole;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ERole userRole;

    public Role(ERole userRole) {
        this.userRole = userRole;
    }
}
