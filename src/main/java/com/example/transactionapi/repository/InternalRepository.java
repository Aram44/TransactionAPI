package com.example.transactionapi.repository;

import com.example.transactionapi.models.Internal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternalRepository extends JpaRepository<Internal, Integer> {
    Internal findByTid(Integer id);
}