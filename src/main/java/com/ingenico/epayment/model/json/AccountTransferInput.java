package com.ingenico.epayment.model.json;

import java.math.BigDecimal;

/**
 * Created by selmantayyar on 8/1/2018.
 */
public class AccountTransferInput {

    private Long accountFromId;

    private Long accountToId;

    private BigDecimal amount;

    public Long getAccountFromId() {
        return accountFromId;
    }

    public void setAccountFromId(Long accountFromId) {
        this.accountFromId = accountFromId;
    }

    public Long getAccountToId() {
        return accountToId;
    }

    public void setAccountToId(Long accountToId) {
        this.accountToId = accountToId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
