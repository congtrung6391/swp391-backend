package com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceImplement;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.ERole;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.Role;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.request.userRequest.UpdateProfileRequest;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.userResponse.UserInformationResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.userResponse.UserProfileResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.role.RoleRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.user.UserRepository;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceInterface.UserManagementInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@Service
@Slf4j
public class UserManagementImplement implements UserManagementInterface {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public User getUser(String username) {
        log.info("Fetching user {} ", username);
        return userRepository.findByUsername(username).orElseThrow(() -> {
            throw new NoSuchElementException("User cannot be found.");
        });
    }

    @Override
    public User getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("Not found user");
                });
        return user;
    }

    @Override
    public UserProfileResponse getUserProfile(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("Not found user");
                });
        return new UserProfileResponse(
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getGrade(),
                user.getAddress(),
                user.getFacebookUrl(),
                user.getAffiliate(),
                user.getGpa(),
                user.getGender()
        );
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User updateUser(String accessToken, Long id, UpdateProfileRequest updateProfileRequest) throws Exception {
        accessToken = accessToken.replaceAll("Bearer ", "");
        User user = userRepository.findByAuthorizationToken(accessToken)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("Not found user");
                });
        if (user.getId() != id) {
            throw new IllegalStateException("You are not allowed to change other people's accounts");
        }

        if (!updateProfileRequest.getPhone().equals(user.getPhone())) {
            if (updateProfileRequest.getPhone() == null) {
                user.setPhone(user.getPhone());
            }else {
                user.setPhone(updateProfileRequest.getPhone());
            }
        }
        if (!updateProfileRequest.getFullName().equals(user.getFullName())) {
            if (updateProfileRequest.getFullName().isEmpty()) {
                user.setFullName(user.getFullName());
            }else {
                user.setFullName(updateProfileRequest.getFullName());
            }
        }
        if (!updateProfileRequest.getGrade().equals(user.getGrade())) {
            String grade = Integer.toString(updateProfileRequest.getGrade());
            if (grade.isEmpty()) {
                user.setGrade(user.getGrade());
            }else {
                user.setGrade(updateProfileRequest.getGrade());
            }
        }
        if (!updateProfileRequest.getAddress().equals(user.getAddress())) {
            if (updateProfileRequest.getAddress().isEmpty()) {
                user.setAddress(user.getAddress());
            }else {
                user.setAddress(updateProfileRequest.getAddress());
            }
        }

        if (!updateProfileRequest.getAffiliate().equals(user.getAffiliate())) {
            if (updateProfileRequest.getAddress().isEmpty()) {
                user.setAffiliate(user.getAffiliate());
            }else {
                user.setAffiliate(updateProfileRequest.getAffiliate());
            }
        }

        if (!updateProfileRequest.getAvatar().equals(user.getAvatar())) {
            if (updateProfileRequest.getAvatar().isEmpty()) {
                user.setAvatar(user.getAvatar());
            }else {
                user.setAvatar(updateProfileRequest.getAffiliate());
            }
        }

        if (!updateProfileRequest.getFacebookUrl().equals(user.getFacebookUrl())) {
            if (updateProfileRequest.getFacebookUrl().isEmpty()) {
                user.setFacebookUrl(user.getFacebookUrl());
            }else {
                user.setFacebookUrl(updateProfileRequest.getFacebookUrl());
            }
        }

        if (!updateProfileRequest.getGender().equals(user.getGender())) {
            if (updateProfileRequest.getGender().isEmpty()) {
                user.setGender(user.getGender());
            }else {
            user.setGender(updateProfileRequest.getGender());}
        }
        if (!updateProfileRequest.getGpa().equals(user.getGpa())) {
            String gpa = Double.toString(updateProfileRequest.getGpa());
            if (gpa.isEmpty()) {
                user.setGpa(user.getGpa());
            }else {
            user.setGpa(updateProfileRequest.getGpa());}
        }

        if (!updateProfileRequest.getBirthday().equals(user.getBirthday())) {
            if (updateProfileRequest.getBirthday().isEmpty()) {
                user.setBirthday(user.getBirthday());
            }else {
            user.setBirthday(updateProfileRequest.getBirthday());}
        }

        /*
       //   updateProfileRequest.setPassword(encoder.encode(updateProfileRequest.getPassword()));
        updateProfileRequest.setNewPassword(encoder.encode(updateProfileRequest.getNewPassword()));
        if (!updateProfileRequest.getNewPassword().equals(user.getPassword())) {
            if (updateProfileRequest.getNewPassword().isEmpty()) {
                user.setPassword(user.getPassword());
            }

            if (!encoder.encode(updateProfileRequest.getPassword()).equals(user.getPassword())) {
                throw new Exception("The old password is not correct");
            }
            user.setPassword(updateProfileRequest.getNewPassword());
        }

         */

        userRepository.save(user);
        return user;

    }

    @Override
    public List<UserInformationResponse> getAllUser(Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        List<User> users = userRepository.findAllByStatusIsTrueOrderByIdDesc(pageable);
        List<UserInformationResponse> userList = new ArrayList<>();
        for (User user : users) {
            UserInformationResponse response = new UserInformationResponse(user);
            userList.add(response);
        }
        return userList;
    }

    @Override
    public void createAccount(User user) {
        userRepository.save(user);
    }

    @Override
    public void saveRole(Role role) {
        roleRepository.save(role);
    }

    @Override
    public void deleteUser(Long id) throws Exception {
        User user = userRepository.findById(id).get();
        Role role = roleRepository.findByUserRole(ERole.SUPER_ADMIN).get();
        if (user.getRoles().contains(role)) {
            throw new Exception();
        } else {
            user.setStatus(false);
            userRepository.save(user);
        }
    }

    @Override
    public List<UserInformationResponse> getListTutor(Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Role role = roleRepository.findByUserRole(ERole.TUTOR)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("Not found role");
                });
        List<User> users = userRepository.findAllByRolesAndStatusIsTrueOrderByIdDesc(role, pageable);
        List<UserInformationResponse> tutorList = new ArrayList<>();
        for (User user : users) {
            UserInformationResponse response = new UserInformationResponse(user);
            tutorList.add(response);
        }
        return tutorList;
    }

    //admin search user - by Nam
    @Override
    public List<UserInformationResponse> adminSearchUser(String id, String name, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of((page - 1), limit);
        List<User> users = new ArrayList<>();
        if (name == null || name.isEmpty()) {
            users = userRepository.findAllByIdAndStatusIsTrue(Long.parseLong(id), pageable).
                    orElseThrow(() -> {
                        throw new NoSuchElementException("Can't find users that match the search value");
                    });
        } else if (id != null && !name.isEmpty() && name != null) {
            users = userRepository.findByIdAndName(Long.parseLong(id), "%" + name + "%", "%" + name + "%", "%" + name + "%", pageable)
                    .orElseThrow(() -> {
                        throw new NoSuchElementException("Can't find users that match the search value");
                    });

        } else {
            users = userRepository.findAllByName("%" + name + "%", "%" + name + "%", "%" + name + "%", pageable)
                    .orElseThrow(() -> {
                        throw new NoSuchElementException("Can't find users that match the search value");
                    });
        }
        List<UserInformationResponse> responseList = new ArrayList<>();
        for (User user : users) {
            UserInformationResponse response = new UserInformationResponse(user);
            responseList.add(response);
        }
        return responseList;
    }

    //public search tutor - by Nam
    @Override
    public List<UserInformationResponse> publicSearchUser(String name, Integer page, Integer limit) {
        Role role = roleRepository.findByUserRole(ERole.TUTOR).get();

        page -= 1;
        Pageable pageable = PageRequest.of(page, limit);

        Page<User> users = userRepository.
                findAllByStatusIsTrueAndRolesAndEmailContainsOrRolesAndFullNameContainingOrderByIdDesc
                        (role, name, role, name, pageable);

        List<UserInformationResponse> responseList = new ArrayList<>();

        for (User user : users.toList()) {
            UserInformationResponse response = new UserInformationResponse(user);
            responseList.add(response);
        }
        return responseList;
    }
}


