package com.example.transactionapi.repository;


import com.example.transactionapi.models.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findAllBySenderOrReceiver(Integer sender, Integer receiver);
    Page<Transaction> findAllBySendtimeBetween(LocalDateTime sendTimeStart, LocalDateTime sendTimeEnd,Pageable pageable);
    List<Transaction> findAllBySenderOrReceiverAndSendtimeBetween(Integer sender, Integer receiver,LocalDateTime sendTimeStart, LocalDateTime sendTimeEnd);

}