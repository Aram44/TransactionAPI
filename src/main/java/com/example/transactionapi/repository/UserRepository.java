package com.example.transactionapi.repository;


import com.example.transactionapi.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
    @Query("FROM User WHERE email=:email")
    User findByEmail(@Param("email") String email);

    Page<User> findAllByOrderByIdDesc(Pageable pageable);
}
