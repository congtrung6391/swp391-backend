package com.swp391.onlinetutorapplication.onlinetutorapplication.repository.user;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.Role;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    List<User> findAllByRoles(String role);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
    Optional<User> findByActivateToken(String token);
    Optional<User> findByResetPasswordCode(Long code);
    Optional<User> findByAuthorizationToken(String accessToken);
    List<User> findAllByStatusIsTrue();
    List<User> findAllByRolesAndStatusIsTrue(Role role);
    Optional<User> findByIdAndStatusIsTrue(Long id);
    @Override
    List<User> findAll();
    Optional<List<User>> findAllByIdAndStatusIsTrue(Long id);
    @Query("SELECT distinct u from User u where u.id= ?1 and (u.email like ?2 or u.fullName like ?3 or u.username like ?4)")
    Optional<List<User>> findByIdAndName(Long id, String email, String fullName, String userName);

    @Query("SELECT distinct u from User u where u.email like ?1 or u.fullName like ?2 or u.username like ?3")
    Optional<List<User>> findAllByName(String email, String fullName, String userName);

    Optional<List<User>> findAllByStatusIsTrueAndRolesAndEmailContainsOrRolesAndFullNameContaining
            (Role role, String email, Role role2, String fullName);
}
