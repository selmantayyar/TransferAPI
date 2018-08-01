package com.ingenico.epayment.model.json;

public class AccountSuccessResponse extends SuccessResponse {

    private Long accountId;

    public AccountSuccessResponse(String message, Long id) {
        super(message);
        accountId=id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}
