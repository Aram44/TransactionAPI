package com.example.transactionapi.repository.loan;


import com.example.transactionapi.model.loan.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
}