package com.swp391.onlinetutorapplication.onlinetutorapplication.model.userDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserDetailsImplement implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String email;

    @JsonIgnore // Tránh việc trả về password dưới dạng JSON
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImplement(Long id, String username, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    //dùng để build user sau khi đăng nhập, lấy các role của user
    //Vì user có thể có nhiều role nên phải map vào trong một list
    //Sau khi build xong sẽ trả về ID, username, email, password, authorities
    public static UserDetailsImplement build(User user){
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getUserRole().name()))
                .collect(Collectors.toList());
        return new UserDetailsImplement(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities; // trả về list role của user
    }

    public Long getId(){
        return id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getEmail(){
        return email;
    }

    //Tất cả để true, user chỉ việc đăng kí thành công sẽ dùng được tài khoản ngay
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImplement user = (UserDetailsImplement) o;
        return Objects.equals(id, user.id);
    }

}
