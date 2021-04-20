package com.example.transactionapi.repository.user;

import com.example.transactionapi.models.utils.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, Integer> {
    @Query("FROM Account WHERE uid=:uid")
    List<Account> findAllByUId(@Param("uid") Integer uid);

    Page<Account> findByUid(Integer uid, Pageable pageable);
}
