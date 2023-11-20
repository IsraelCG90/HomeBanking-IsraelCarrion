package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dto.ClientLoanDTO;
import com.mindhub.homebanking.models.ClientLoan;

public interface ClientLoanService {
    void saveClientLoan(ClientLoan clientLoan);
    ClientLoan findLoanById(Long id);
}
