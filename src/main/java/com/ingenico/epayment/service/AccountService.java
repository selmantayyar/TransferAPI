package com.ingenico.epayment.service;

import com.ingenico.epayment.entity.Account;


public interface AccountService {

    Account createAccountWithBalance(Account account);

    boolean transferAmount();

    boolean validateAccountData(Account account);
}
