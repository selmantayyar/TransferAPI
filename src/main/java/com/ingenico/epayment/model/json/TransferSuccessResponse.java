package com.ingenico.epayment.model.json;

public class TransferSuccessResponse extends SuccessResponse {

    private Long accountFromId;

    private Long accountToId;

    public TransferSuccessResponse(String message, Long fromId,Long toId) {
        super(message);
        accountFromId=fromId;
        accountToId=toId;
    }

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
}
