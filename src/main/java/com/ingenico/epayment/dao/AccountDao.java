package com.ingenico.epayment.dao;

import com.ingenico.epayment.entity.Account;
import java.io.Serializable;
import java.util.List;


public interface AccountDao {

    Account persist(Account account);

    /**
     * @param serializable
     * @return true if an object with the given serializable exists
     */
    boolean exists(Serializable serializable);

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

}
