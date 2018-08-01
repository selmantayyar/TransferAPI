package com.ingenico.epayment.model.json;

public class AccountSuccessResponse {

    private String message;
    private Long accountId;

    public AccountSuccessResponse(String message, Long id) {
        this.message=message;
        accountId=id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}
