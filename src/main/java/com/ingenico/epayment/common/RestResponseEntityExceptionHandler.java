package com.ingenico.epayment.common;

import com.ingenico.epayment.model.json.JSONResponseDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/** Global exception handler.Unhandled exceptions will fall into this class.
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);
    /**
     * Method handling internal errors.
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        LOG.error("Server Exception Occurred",ex);

        JSONResponseDocument jsonResponseDocument = new JSONResponseDocument<>();
        jsonResponseDocument.getErrors().add(TransferApiConstants.STR_ERROR_MESSAGE_INTERNAL_SERVER_ERROR);

        return handleExceptionInternal(ex, jsonResponseDocument,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}