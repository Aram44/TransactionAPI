package com.example.transactionapi.repository.user;

import com.example.transactionapi.model.user.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query(value = "SELECT SUM(balance) FROM account GROUP BY currency", nativeQuery = true)
    List<Double> currencyAndBalanceGroup();
//    Page<Account> findAllByUidOrderByIdDesc(Integer uid, Pageable pageable);
//    Page<Account> findAllByCurrencyOrderByIdDesc(Currency currency, Pageable pageable);
//    Page<Account> findAllByUidAndCurrencyOrderByIdDesc(Integer uid, Currency currency, Pageable pageable);
    Page<Account> findAllByOrderByIdDesc(Pageable pageable);
    @Query("FROM account WHERE user.id=:uid")
    List<Account> findByUId(@Param("uid") long uid);
}
