package com.example.transactionapi.repository.loan;

import com.example.transactionapi.model.loan.Loan;
import com.example.transactionapi.model.loan.LoanPage;
import com.example.transactionapi.model.loan.LoanSearchCriteria;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class LoanCriteriaRepository {
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public LoanCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<Loan> findAllWithFilters(LoanPage LoanPage,
                                         LoanSearchCriteria searchCriteria){
        CriteriaQuery<Loan> query = criteriaBuilder.createQuery(Loan.class);
        Root<Loan> LoanRoot = query.from(Loan.class);
        Predicate predicate = getPrediacate(searchCriteria, LoanRoot);
        query.where(predicate);
        setOrder(LoanPage, query, LoanRoot);

        TypedQuery<Loan> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(LoanPage.getPage() * LoanPage.getPageSize());
        typedQuery.setMaxResults(LoanPage.getPageSize());
        Pageable pageable = getPageable(LoanPage);
        long LoansCount = getLoansCount(predicate);

        return new PageImpl<>(typedQuery.getResultList(), pageable, LoansCount);
    }


    private Pageable getPageable(LoanPage LoanPage) {
        Sort sort = Sort.by(LoanPage.getDirection(), LoanPage.getSortBy());
        return PageRequest.of(LoanPage.getPage(), LoanPage.getPageSize(), sort);
    }

    private Predicate getPrediacate(LoanSearchCriteria searchCriteria,
                                    Root<Loan> LoanRoot) {
        List<Predicate> predicates = new ArrayList<>();
        if (Objects.nonNull(searchCriteria.getFrom())){
            predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(LoanRoot.get("requesttime"),searchCriteria.getFrom())
                    );
        }
        if (Objects.nonNull(searchCriteria.getTo())){
            predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(LoanRoot.get("requesttime"),searchCriteria.getTo())
            );
        }
        if (Objects.nonNull(searchCriteria.getStatus())){
            predicates.add(
                    criteriaBuilder.equal(LoanRoot.get("status"),searchCriteria.getStatus())
            );
        }
        if (Objects.nonNull(searchCriteria.getUser())){
            predicates.add(
                    criteriaBuilder.equal(LoanRoot.get("user"), searchCriteria.getUser())
            );
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(LoanPage LoanPage,
                          CriteriaQuery<Loan> query,
                          Root<Loan> LoanRoot) {
        if (LoanPage.getDirection().equals(Sort.Direction.DESC)){
            query.orderBy(criteriaBuilder.desc(LoanRoot.get(LoanPage.getSortBy())));
        }else{
            query.orderBy(criteriaBuilder.asc(LoanRoot.get(LoanPage.getSortBy())));
        }
    }

    private long getLoansCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Loan> countRoot = countQuery.from(Loan.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
