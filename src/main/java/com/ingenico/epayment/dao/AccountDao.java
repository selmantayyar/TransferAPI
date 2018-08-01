package com.ingenico.epayment.dao;

import com.ingenico.epayment.entity.Account;
import java.io.Serializable;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;


public interface AccountDao {

    Account persist(Account account);

    /**
     * saves or updates the object
     *
     * @param account of type Account
     */
    Account saveOrUpdate(Account account);


    /**
     * @return all of T otjects
     */
    List<Account> getAll();

    /**
     * @param serializable to get
     * @return the object
     */
    Account get(Serializable serializable);

    Account getWithLock(Serializable serializable);

    @Transactional
    void removeAll();

    void flushAndClear();

}
