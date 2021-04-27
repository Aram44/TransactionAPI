package com.example.transactionapi.repository;


import com.example.transactionapi.models.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    Page<Schedule> findAllByLid(Integer lid, Pageable pageable);
    List<Schedule> findAllByLid(Integer lid);
    Schedule findByLidAndMonth(Integer lid, int month);
}