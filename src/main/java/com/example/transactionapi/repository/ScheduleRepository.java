package com.example.transactionapi.repository;


import com.example.transactionapi.models.Schedule;
import com.example.transactionapi.models.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    Page<Schedule> findAllByLid(Integer lid, Pageable pageable);
    List<Schedule> findAllByLid(Integer lid);
    Schedule findByLidAndMonth(Integer lid, int month);
    List<Schedule> findAllByPaymantdateBetweenAndStatusNotOrderById(LocalDateTime start, LocalDateTime end, Status status);
}