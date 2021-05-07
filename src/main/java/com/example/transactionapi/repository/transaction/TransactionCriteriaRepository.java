package com.example.transactionapi.repository.transaction;

import com.example.transactionapi.model.transaction.Transaction;
import com.example.transactionapi.model.transaction.TransactionPage;
import com.example.transactionapi.model.transaction.TransactionSearchCriteria;
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
public class TransactionCriteriaRepository {
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public TransactionCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<Transaction> findAllWithFilters(TransactionPage transactionPage,
                                                TransactionSearchCriteria searchCriteria){
        CriteriaQuery<Transaction> query = criteriaBuilder.createQuery(Transaction.class);
        Root<Transaction> transactionRoot = query.from(Transaction.class);
        Predicate predicate = getPrediacate(searchCriteria, transactionRoot);
        query.where(predicate);
        setOrder(transactionPage, query, transactionRoot);

        TypedQuery<Transaction> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(transactionPage.getPage() * transactionPage.getPageSize());
        typedQuery.setMaxResults(transactionPage.getPageSize());
        Pageable pageable = getPageable(transactionPage);
        long transactionsCount = getTransactionsCount(predicate);

        return new PageImpl<>(typedQuery.getResultList(), pageable, transactionsCount);
    }


    private Pageable getPageable(TransactionPage transactionPage) {
        Sort sort = Sort.by(transactionPage.getDirection(), transactionPage.getSortBy());
        return PageRequest.of(transactionPage.getPage(), transactionPage.getPageSize(), sort);
    }

    private Predicate getPrediacate(TransactionSearchCriteria searchCriteria,
                                    Root<Transaction> transactionRoot) {
        List<Predicate> predicates = new ArrayList<>();
        if (Objects.nonNull(searchCriteria.getFrom())){
            predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(transactionRoot.get("sendtime"),searchCriteria.getFrom())
                    );
        }
        if (Objects.nonNull(searchCriteria.getTo())){
            predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(transactionRoot.get("sendtime"),searchCriteria.getTo())
            );
        }
        if (Objects.nonNull(searchCriteria.getStatus())){
            predicates.add(
                    criteriaBuilder.equal(transactionRoot.get("status"),searchCriteria.getStatus())
            );
        }
        if (Objects.nonNull(searchCriteria.getUser())){
            predicates.add(
                    criteriaBuilder.equal(transactionRoot.get("sender").get("user"), searchCriteria.getUser())
            );
            predicates.add(
                    criteriaBuilder.equal(transactionRoot.get("receiver").get("user"), searchCriteria.getUser())
            );
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(TransactionPage transactionPage,
                          CriteriaQuery<Transaction> query,
                          Root<Transaction> transactionRoot) {
        if (transactionPage.getDirection().equals(Sort.Direction.DESC)){
            query.orderBy(criteriaBuilder.desc(transactionRoot.get(transactionPage.getSortBy())));
        }else{
            query.orderBy(criteriaBuilder.asc(transactionRoot.get(transactionPage.getSortBy())));
        }
    }

    private long getTransactionsCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Transaction> countRoot = countQuery.from(Transaction.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
