package com.example.transactionapi.repository;


import com.example.transactionapi.models.Loan;
import com.example.transactionapi.models.utils.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface LoanRepository extends JpaRepository<Loan, Integer> {
    Page<Loan> findAllByStatus(Status status, Pageable pageable);
    Page<Loan> findAllByRequesttimeBetween(LocalDateTime dateStart, LocalDateTime dateFnd, Pageable pageable);
    Page<Loan> findAllByStatusAndRequesttimeBetween(Status state, LocalDateTime dateStart, LocalDateTime dateEnd, Pageable pageable);
    List<Loan> findAllByAid(Integer aid);
    List<Loan> findAllByAidAndRequesttimeBetween(Integer id, LocalDateTime dateStart, LocalDateTime dateEnd);
}