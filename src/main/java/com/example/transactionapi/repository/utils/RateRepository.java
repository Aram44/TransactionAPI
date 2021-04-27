package com.example.transactionapi.repository.utils;


import com.example.transactionapi.models.utils.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RateRepository extends JpaRepository<Rate, Integer> {
    Rate findTopByOrderByIdDesc();
}