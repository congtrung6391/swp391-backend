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

@Repository
public class CourseCriteriaRepository {
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public CourseCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<Course> findWithFilter(CoursePage coursePage,
                                       CourseSearchCriteria courseSearchCriteria) {
        CriteriaQuery<Course> criteriaQuery = criteriaBuilder.createQuery(Course.class);
        Root<Course> courseRoot = criteriaQuery.from(Course.class);
        Join<Course, User> courseUserJoin = courseRoot.join(Course_.tutor, JoinType.INNER);
        Join<Course, Subject> courseSubjectJoin = courseRoot.join(Course_.subject, JoinType.INNER);
        Predicate predicate = getPredicate(courseSearchCriteria,
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

    private Predicate getPredicate(CourseSearchCriteria courseSearchCriteria,
                                   Root<Course> courseRoot,
                                   Join<Course, User> courseUserJoin,
                                   Join<Course, Subject> courseSubjectJoin) {
        List<Predicate> predicates = new ArrayList<>();
        if (Objects.nonNull(courseSearchCriteria.getId())) {
            predicates.add(
                    criteriaBuilder.equal(courseRoot.get(Course_.ID),
                            courseSearchCriteria.getId())
            );
        }
        if (Objects.nonNull(courseSearchCriteria.getCourseName())) {
            predicates.add(
                    criteriaBuilder.like(courseRoot.get(Course_.COURSE_NAME),
                            "%" + courseSearchCriteria.getCourseName() + "%")
            );
        }
        if (Objects.nonNull(courseSearchCriteria.getTutorName())) {
            predicates.add(
                    criteriaBuilder.like(courseUserJoin.get(User_.FULL_NAME),
                           "%" + courseSearchCriteria.getTutorName() + "%")
            );
        }
        if (Objects.nonNull(courseSearchCriteria.getSubjectId())) {
            predicates.add(
                    criteriaBuilder.equal(courseSubjectJoin.get(Subject_.ID),
                            courseSearchCriteria.getSubjectId())
            );
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(CoursePage coursePage, CriteriaQuery<Course> criteriaQuery, Root<Course> courseRoot) {
        if (coursePage.getSortDirection().equals(Sort.Direction.DESC)) {
            criteriaQuery.orderBy(criteriaBuilder.desc(courseRoot.get(coursePage.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(courseRoot.get(coursePage.getSortBy())));
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
