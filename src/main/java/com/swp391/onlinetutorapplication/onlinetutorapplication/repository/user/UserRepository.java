package com.swp391.onlinetutorapplication.onlinetutorapplication.repository.user;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.Role;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findAllByRoles(String role);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

    Optional<User> findByActivateToken(String token);

    Optional<User> findByResetPasswordCode(Long code);

    Optional<User> findByAuthorizationToken(String accessToken);

    Page<User> findAllByStatusIsTrueOrderByIdDesc(Pageable pageable);

    Page<User> findAllByRolesAndStatusIsTrueOrderByIdDesc(Role role, Pageable pageable);

    Optional<User> findByIdAndStatusIsTrue(Long id);

    @Override
    List<User> findAll();

    Optional<Page<User>> findAllByIdAndStatusIsTrue(Long id, Pageable pageable);

    @Query("SELECT distinct u from User u where u.id= ?1 and (u.email like ?2 or u.fullName like ?3 or u.username like ?4) and u.status=true  order by u.id desc ")
    Optional<Page<User>> findByIdAndName(Long id, String email, String fullName, String userName, Pageable pageable);

    @Query("SELECT distinct u from User u where (u.email like ?1 or u.fullName like ?2 or u.username like ?3) and u.status=true order by u.id desc ")
    Optional<Page<User>> findAllByName(String email, String fullName, String userName, Pageable pageable);

    @Override
    Page<User> findAll(Pageable pageable);

    Page<User> findAllByStatusIsTrueAndRolesAndEmailContainsOrRolesAndFullNameContainingOrderByIdDesc
            (Role role, String email, Role role2, String fullName, Pageable pageable);

    @Query("select u.id from User u join Rate r on u.id=r.tutor.id group by u.id order by avg(r.value) desc ")
    Page<Long> sortingTutorByRating(Pageable pageable);
}
