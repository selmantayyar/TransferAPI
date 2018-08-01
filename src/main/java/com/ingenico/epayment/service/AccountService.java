package com.ingenico.epayment.service;

import com.ingenico.epayment.entity.Account;
import com.ingenico.epayment.model.json.AccountTransferInput;


public interface AccountService {

    Account createAccountWithBalance(Account account);

    /**
     * Method validating data and performing the transfer.
     * <br/>
     * Accounts should already exist.
     * <br/>
     * Account debiting the money should not end up with balance <0
     * @param accountTransferInput
     * @return
     */
    boolean transferAmount(AccountTransferInput  accountTransferInput);

    /**
     * Method validating account data.
     * <br/>
     * Account should not exist already.
     * <br/>
     * Account should not have minus balance and should have a name.
     * @param account
     * @return
     */
    boolean validateAccountData(Account account);

    /**
     * Method validating account transfer data.
     * <br/>
     * Account IDs'should not be null
     * @param accountTransferInput
     * @return
     */
    boolean validateTransferData(AccountTransferInput accountTransferInput);

    Account getAccount(Long accountId);
}
