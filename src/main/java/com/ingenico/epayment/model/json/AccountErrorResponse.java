package com.ingenico.epayment.model.json;


public class AccountErrorResponse {

    private Long accountId;
    private String errorCode;
    private String errorMessage;

    public AccountErrorResponse(Long accountId, String errorCode, String errorMessage) {
        this.accountId = accountId;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
