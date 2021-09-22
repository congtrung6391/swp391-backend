package com.swp391.onlinetutorapplication.onlinetutorapplication.repository.user;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findAllByRoles(String role);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);

    @Transactional
    @Modifying
    @Query("update User u set u.activeStatus=true where u.activateToken=?1")
    void activeUser(String activateToken);

    @Transactional
    @Modifying
    @Query("update User u set u.password = ?1 where u.username=?2")
    void changePassword(String username);

}
