package com.example.transactionapi.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShowService<T> {
    Page<T> findAll(Pageable pageable);

    T findById(Integer id);

    T saveOrUpdate(T t);

    String deleteById(Integer id);
}
