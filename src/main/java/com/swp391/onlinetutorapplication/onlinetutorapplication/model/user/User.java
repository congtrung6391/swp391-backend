package com.swp391.onlinetutorapplication.onlinetutorapplication.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.*;

import static javax.persistence.FetchType.EAGER;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users_account",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        }
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;
    private String username;
    private String email;
    private String fullName;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String phone;
    private Integer grade;
    private String address;
    private String facebookUrl;
    private String affiliate;
    private Double gpa;
    private String gender;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String authorizationToken;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Instant expireAuthorization;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String activateToken;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean activeStatus = false;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean status = true;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long resetPasswordCode;
    @ManyToMany(fetch = EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<Role> roles = new HashSet<>();

    public User(String username, String email, String phone, String fullName, String password, String activateToken) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.fullName = fullName;
        this.password = password;
        this.activateToken = activateToken;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

}
