package com.example.transactionapi.repository;


import com.example.transactionapi.models.Loan;
import com.example.transactionapi.models.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface LoanRepository extends JpaRepository<Loan, Integer> {
    Page<Loan> findAllByOrderByIdDesc(Pageable pageable);
    Page<Loan> findAllByStatusOrderByIdDesc(Status status, Pageable pageable);
    Page<Loan> findAllByRequesttimeBetweenOrderByIdDesc(LocalDateTime dateStart, LocalDateTime dateFnd, Pageable pageable);
    Page<Loan> findAllByStatusAndRequesttimeBetweenOrderByIdDesc(Status state, LocalDateTime dateStart, LocalDateTime dateEnd, Pageable pageable);
    Page<Loan> findAllByUidOrderByIdDesc(Integer uid, Pageable pageable);
    Page<Loan> findAllByUidAndRequesttimeBetweenOrderByIdDesc(Integer uid, LocalDateTime dateStart, LocalDateTime dateEnd, Pageable pageable);
    @Query(value = "SELECT status,COUNT(id) FROM loan GROUP BY status ORDER BY status", nativeQuery = true)
    List<Object[]> statusGroup();
}