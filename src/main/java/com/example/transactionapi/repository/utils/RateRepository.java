package com.example.transactionapi.repository.utils;


import com.example.transactionapi.model.utils.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;


@Repository
public interface RateRepository extends JpaRepository<Rate, Integer> {
    Rate findTopByOrderByIdDesc();
    Rate findByUpdatedtime(LocalDateTime time);
}