package com.example.transactionapi.repository;


import com.example.transactionapi.models.Loan;
import com.example.transactionapi.models.Schedule;
import com.example.transactionapi.models.utils.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    Page<Schedule> findAllByLid(Integer lid, Pageable pageable);
    Schedule findByMonth(Integer month);
}