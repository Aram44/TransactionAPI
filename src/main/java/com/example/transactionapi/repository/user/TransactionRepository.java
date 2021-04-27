package com.example.transactionapi.repository.user;


import com.example.transactionapi.models.enums.Status;
import com.example.transactionapi.models.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Page<Transaction> findAllByOrderByIdDesc(Pageable pageable);
    Page<Transaction> findAllBySendtimeBetweenOrderByIdDesc(LocalDateTime sendTimeStart, LocalDateTime sendTimeEnd,Pageable pageable);
    Page<Transaction> findAllByStatusAndSendtimeBetweenOrderByIdDesc(Status status, LocalDateTime sendTimeStart, LocalDateTime sendTimeEnd, Pageable pageable);
    Page<Transaction> findAllByStatusOrderByIdDesc(Status status, Pageable pageable);
    Page<Transaction> findBySidOrRidOrderByIdDesc(Integer sid, Integer rid, Pageable pageable);
    Page<Transaction> findAllByStatusAndSidOrderByIdDesc(Status state,Integer sid,Pageable pageable);
    Page<Transaction> findAllByStatusAndSidAndSendtimeBetweenOrderByIdDesc(Status state, Integer sid, LocalDateTime dateStart, LocalDateTime dateFinish,Pageable pageable);
    Page<Transaction> findAllBySidAndSendtimeBetweenOrderByIdDesc(Integer sid, LocalDateTime dateStart, LocalDateTime dateFinish, Pageable pageable);
}