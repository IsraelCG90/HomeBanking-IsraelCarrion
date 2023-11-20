package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dto.LoanDTO;
import com.mindhub.homebanking.models.Loan;

import java.util.List;
import java.util.Set;

public interface LoanService {
    List<Loan> getAllLoan();

    Set<LoanDTO> getAllLoanDto();

    boolean existsLoanById(Long id);

    Loan findLoanById(Long id);

    void saveLoan(Loan loan);

    boolean existsLoanByName(String name);
}
