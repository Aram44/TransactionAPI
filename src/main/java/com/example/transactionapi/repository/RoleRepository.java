package com.example.transactionapi.repository;

import com.example.transactionapi.models.Role;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends PagingAndSortingRepository<Role, Integer> {

}