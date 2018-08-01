package com.ingenico.epayment.model.json;

/**
 * Created by selmantayyar on 8/1/2018.
 */
public class SuccessResponse {
    protected String message;

    public SuccessResponse(String message) {
        this.message=message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
