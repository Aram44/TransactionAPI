package com.example.transactionapi.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
public interface Resource<T> {
    @GetMapping
    ResponseEntity<Page<T>> findAll(Pageable pageable);

    @GetMapping("{id}")
    ResponseEntity<T> findByID(@PathVariable Integer id);

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<T> save(@RequestBody T t);

    @PutMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<T> update(@RequestBody T t);

    @DeleteMapping("{id}")
    ResponseEntity<String> deleteById(@PathVariable Integer id);
}
