package com.example.transactionapi.repository.user;


import com.example.transactionapi.models.utils.Status;
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
    Page<Transaction> findAllByStatusAndSendtimeBetween(Status status, LocalDateTime sendTimeStart, LocalDateTime sendTimeEnd, Pageable pageable);
    Page<Transaction> findAllByStatus(Status status, Pageable pageable);
    List<Transaction> findAllBySenderOrReceiverAndSendtimeBetween(Integer sender, Integer receiver,LocalDateTime sendTimeStart, LocalDateTime sendTimeEnd);

}