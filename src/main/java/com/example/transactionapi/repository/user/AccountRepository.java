package com.example.transactionapi.repository.user;

import com.example.transactionapi.models.enums.Currency;
import com.example.transactionapi.models.utils.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    @Query("FROM Account WHERE uid=:uid")
    List<Account> findAllByUId(@Param("uid") Integer uid);
    @Query(value = "SELECT SUM(balance) FROM account GROUP BY currency", nativeQuery = true)
    List<Double> currencyAndBalanceGroup();
    Page<Account> findAllByUidOrderByIdDesc(Integer uid, Pageable pageable);
    Page<Account> findAllByCurrencyOrderByIdDesc(Currency currency, Pageable pageable);
    Page<Account> findAllByUidAndCurrencyOrderByIdDesc(Integer uid, Currency currency, Pageable pageable);
    Page<Account> findAllByOrderByIdDesc(Pageable pageable);
}
