package com.example.transactionapi.repository.user;

import com.example.transactionapi.model.user.Role;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends PagingAndSortingRepository<Role, Integer> {

}