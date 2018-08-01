package com.ingenico.epayment.common;


public class TransferApiConstants {

    public static final String STR_ERROR_MESSAGE_ACCOUNT_VALIDATION = "Account can not be validated.Account should "
            + "not already exist and amount should not be less than zero";
    public static final String STR_ERROR_MESSAGE_TRANSFER_VALIDATION = "Transfer can not be done.Both accounts should"
            + " exist and Account that is crediting the amount can not be overdrawn";
    public static final String STR_WS_GENERIC_ERROR_CODE = "ERROR";
    public static final String STR_SUCCESS_MESSAGE_ACCOUNT_CREATION = "Account Created";

    public static final String STR_SUCCESS_MESSAGE_TRANSFER = "Money transfer successfully completed";
    public static final String STR_ERROR_MESSAGE_INTERNAL_SERVER_ERROR = "An internal server error occurred.Please "
            + "contact Support Team." ;

    private TransferApiConstants() {
    }
}
