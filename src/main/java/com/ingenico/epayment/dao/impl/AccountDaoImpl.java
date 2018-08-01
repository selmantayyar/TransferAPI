package com.ingenico.epayment.dao.impl;

import com.ingenico.epayment.dao.AccountDao;
import com.ingenico.epayment.entity.Account;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
@Repository
public class AccountDaoImpl implements AccountDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Account persist(Account account) {
        entityManager.persist(account);
        return account;
    }

    @Override
    public Account saveOrUpdate(Account account) {
        Account newAccount = entityManager.merge(account);
        return newAccount;
    }


    @Override
    public List<Account> getAll() {
        return entityManager.createQuery("select p from " + Account.class.getSimpleName() + " p").getResultList();
    }

    @Override
    public Account get(Serializable serializable) {
        return  entityManager.find(Account.class, serializable);
    }

    @Override
    public Account getWithLock(Serializable serializable) {
        return  entityManager.find(Account.class, serializable,LockModeType.PESSIMISTIC_READ);
    }

    @Override
    @Transactional
    public void removeAll() {
        entityManager.createQuery("delete from " + Account.class.getName()).executeUpdate();
    }

    @Override
    public void flushAndClear() {
       entityManager.flush();
       entityManager.clear();
    }
}
