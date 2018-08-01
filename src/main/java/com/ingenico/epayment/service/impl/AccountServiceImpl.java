package com.ingenico.epayment.service.impl;

import com.ingenico.epayment.dao.AccountDao;
import com.ingenico.epayment.entity.Account;
import com.ingenico.epayment.model.json.AccountTransferInput;
import com.ingenico.epayment.service.AccountService;
import java.math.BigDecimal;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by selmantayyar on 8/1/2018.
 */
@Component
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountDao accountDao;

    @Override
    @Transactional
    public Account createAccountWithBalance(Account account) {
        accountDao.persist(account);
        accountDao.flushAndClear();
        return account;
    }

    @Override
    @Transactional
    public boolean transferAmount(AccountTransferInput accountTransferInput) {

        Account accountFrom=accountDao.getWithLock(accountTransferInput.getAccountFromId());
        Account accountTo=accountDao.getWithLock(accountTransferInput.getAccountToId());

        if(accountFrom ==null || accountTo==null){
            return false;
        }

        BigDecimal accountFromNewAmount=accountFrom.getBalance().subtract(accountTransferInput.getAmount());

        if(accountFromNewAmount.compareTo(BigDecimal.ZERO)<0){
            return false;
        }

        BigDecimal accountToNewAmount=accountTo.getBalance().add(accountTransferInput.getAmount());

        accountFrom.setBalance(accountFromNewAmount);
        accountTo.setBalance(accountToNewAmount);

        accountDao.saveOrUpdate(accountFrom);
        accountDao.saveOrUpdate(accountTo);

        accountDao.flushAndClear();

        return true;

    }

    @Override
    public boolean validateAccountData(Account account) {

        return account.getId()==null && StringUtils.isNotEmpty(account.getName()) && account.getBalance().compareTo
                (BigDecimal.ZERO)>=0;
    }

    @Override
    public boolean validateTransferData(AccountTransferInput accountTransferInput) {

        final Long accountToId = accountTransferInput.getAccountToId();
        final Long accountFromId = accountTransferInput.getAccountFromId();

        if(accountToId ==null || accountFromId==null){
            return false;
        }

        return true;
    }

    @Override
    public Account getAccount(Long accountId) {
        return accountDao.get(accountId);
    }
}
