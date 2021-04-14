package com.example.transactionapi.repository;


import com.example.transactionapi.models.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
    User findByEmail(String username);
    List<User> findByEmailNotContaining(String email);

    List<User> findByRoleAndEmailNotContaining(String role, String username);
}
