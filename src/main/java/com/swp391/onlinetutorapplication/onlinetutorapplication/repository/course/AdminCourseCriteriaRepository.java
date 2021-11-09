package com.swp391.onlinetutorapplication.onlinetutorapplication.repository.course;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.*;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.user.User_;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.swp391.onlinetutorapplication.onlinetutorapplication.model.role.ERole.*;

@Repository
public class AdminCourseCriteriaRepository {
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public AdminCourseCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<Course> findWithFilter(CoursePage coursePage,
                                       AdminCourseSearchCriteria adminCourseSearchCriteria) {
        CriteriaQuery<Course> criteriaQuery = criteriaBuilder.createQuery(Course.class);
        Root<Course> courseRoot = criteriaQuery.from(Course.class);
        Join<Course, User> courseUserJoin = courseRoot.join(Course_.tutor, JoinType.INNER);
        Join<Course, Subject> courseSubjectJoin = courseRoot.join(Course_.subject, JoinType.INNER);
        Predicate predicate = getPredicate(adminCourseSearchCriteria,
                courseRoot,
                courseUserJoin,
                courseSubjectJoin);

        criteriaQuery.where(predicate);
        setOrder(coursePage, criteriaQuery, courseRoot);

        TypedQuery<Course> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(coursePage.getPageNumber() * coursePage.getPageSize());
        typedQuery.setMaxResults(coursePage.getPageSize());

        Pageable pageable = getPageable(coursePage);
        long courseCount = getCourseCount(predicate);

        return new PageImpl<>(typedQuery.getResultList(), pageable, courseCount);
    }

    private Predicate getPredicate(AdminCourseSearchCriteria adminCourseSearchCriteria,
                                   Root<Course> courseRoot,
                                   Join<Course, User> courseUserJoin,
                                   Join<Course, Subject> courseSubjectJoin) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(
                criteriaBuilder.isTrue(courseRoot.get(Course_.STATUS))
        );
        if (adminCourseSearchCriteria.getRole().getUserRole().equals(SUPER_ADMIN)) {//admin thi cho lay theo id
            if (Objects.nonNull(adminCourseSearchCriteria.getId())) {
                predicates.add(
                        criteriaBuilder.equal(courseRoot.get(Course_.ID),
                                adminCourseSearchCriteria.getId())
                );
            }
        }
        if (adminCourseSearchCriteria.getRole().getUserRole().equals(TUTOR)) {//tutor lay theo tutor
            predicates.add(
                    criteriaBuilder.equal(courseRoot.get(Course_.TUTOR),
                            adminCourseSearchCriteria.getUserId())
            );
        }
//        if (adminCourseSearchCriteria.getRole().getUserRole().equals(STUDENT)) {//student lay theo student id va public status true
//            predicates.add(
//                    criteriaBuilder.equal(courseRoot.get(Course_.STUDENT),
//                            adminCourseSearchCriteria.getUserId())
//            );
//            predicates.add(
//                    criteriaBuilder.isTrue(courseRoot.get(Course_.PUBLIC_STATUS))
//            );
//        }
        if (Objects.nonNull(adminCourseSearchCriteria.getCourseName())) {
            predicates.add(
                    criteriaBuilder.like(courseRoot.get(Course_.COURSE_NAME),
                            "%" + adminCourseSearchCriteria.getCourseName() + "%")
            );
        }
        if (Objects.nonNull(adminCourseSearchCriteria.getTutorName())) {
            predicates.add(
                    criteriaBuilder.like(courseUserJoin.get(User_.FULL_NAME),
                            "%" + adminCourseSearchCriteria.getTutorName() + "%")
            );
        }
        if (Objects.nonNull(adminCourseSearchCriteria.getSubjectId())) {
            predicates.add(
                    criteriaBuilder.equal(courseSubjectJoin.get(Subject_.ID),
                            adminCourseSearchCriteria.getSubjectId())
            );
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(CoursePage coursePage, CriteriaQuery<Course> criteriaQuery, Root<Course> courseRoot) {
        if (coursePage.getSortDirection().equals(Sort.Direction.DESC)) {
            criteriaQuery.orderBy(criteriaBuilder.desc(courseRoot.get(Course_.ID)));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(courseRoot.get(Course_.ID)));
        }
    }


    private Pageable getPageable(CoursePage coursePage) {
        Sort sort = Sort.by(coursePage.getSortDirection(), coursePage.getSortBy());
        return PageRequest.of(coursePage.getPageNumber(), coursePage.getPageSize(), sort);
    }

    private long getCourseCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Course> courseRoot = countQuery.from(Course.class);
        courseRoot.join(Course_.tutor, JoinType.INNER);
        courseRoot.join(Course_.subject, JoinType.INNER);
        countQuery.select(criteriaBuilder.count(courseRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
