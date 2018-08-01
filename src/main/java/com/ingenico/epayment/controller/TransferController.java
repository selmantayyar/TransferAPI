package com.ingenico.epayment.controller;

import com.ingenico.epayment.common.TransferApiConstants;
import com.ingenico.epayment.entity.Account;
import com.ingenico.epayment.model.json.*;
import com.ingenico.epayment.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
public class TransferController {

    private static final Logger LOG = LoggerFactory.getLogger(TransferController.class);

    @Autowired
    AccountService accountService;

    @RequestMapping(value = "/accounts", method = RequestMethod.POST)
    public ResponseEntity<JSONResponseDocument> createAccount(HttpServletRequest request,@RequestBody Account
            account) {
        LOG.info("Creating account having name {} and balance {}",account.getName(),
                account.getBalance());

        JSONResponseDocument jsonResponseDocument = new JSONResponseDocument<>();
        boolean validated = accountService.validateAccountData(account);
        if (!validated) {
            String strErrorMessage = TransferApiConstants.STR_ERROR_MESSAGE_ACCOUNT_VALIDATION;
            LOG.info("createAccount having name {} and balance {} rejected . Message is: {} ",account.getName(),
                    account.getBalance(),strErrorMessage);

            AccountErrorResponse accountErrorResponse = new AccountErrorResponse(null,
                    TransferApiConstants.STR_WS_GENERIC_ERROR_CODE, strErrorMessage);
            jsonResponseDocument.getErrors().add(accountErrorResponse);
            return new ResponseEntity(jsonResponseDocument, HttpStatus.BAD_REQUEST);
        }

        Account accountCreated= accountService.createAccountWithBalance(account);
        AccountSuccessResponse accountSuccessResponse= new AccountSuccessResponse(TransferApiConstants
                .STR_SUCCESS_MESSAGE_ACCOUNT_CREATION,accountCreated.getId());
        jsonResponseDocument.setData(accountSuccessResponse);
        return new ResponseEntity(jsonResponseDocument, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public ResponseEntity<JSONResponseDocument> transfer(HttpServletRequest request,@RequestBody AccountTransferInput
            accountTransferInput) {
        LOG.info("Transferring  {} from account of {} to the account of {}",
                accountTransferInput.getAmount(), accountTransferInput.getAccountFromId(), accountTransferInput.getAccountToId());

        JSONResponseDocument jsonResponseDocument = new JSONResponseDocument<>();
        boolean validated = accountService.validateTransferData(accountTransferInput);

        if (!validated) {
            return setValidationError(accountTransferInput, jsonResponseDocument);
        }

        boolean transferOK=accountService.transferAmount(accountTransferInput);

        if (!transferOK) {
            return setValidationError(accountTransferInput, jsonResponseDocument);
        }

        TransferSuccessResponse successResponse= new TransferSuccessResponse(TransferApiConstants
                .STR_SUCCESS_MESSAGE_TRANSFER,accountTransferInput.getAccountFromId(),accountTransferInput.getAccountToId());
        jsonResponseDocument.setData(successResponse);
        return new ResponseEntity(jsonResponseDocument, HttpStatus.OK);
    }

    private ResponseEntity<JSONResponseDocument> setValidationError(
            @RequestBody AccountTransferInput accountTransferInput, JSONResponseDocument jsonResponseDocument) {
        String strErrorMessage = TransferApiConstants.STR_ERROR_MESSAGE_TRANSFER_VALIDATION;
        LOG.info("transfer of {} from account of {} to the account of {} rejected. Message is: {} ",accountTransferInput.getAmount(),
                accountTransferInput.getAccountFromId(), accountTransferInput.getAccountToId(),strErrorMessage);

        AccountErrorResponse accountErrorResponse = new AccountErrorResponse(null,
                TransferApiConstants.STR_WS_GENERIC_ERROR_CODE, strErrorMessage);
        jsonResponseDocument.getErrors().add(accountErrorResponse);
        return new ResponseEntity(jsonResponseDocument, HttpStatus.BAD_REQUEST);
    }

    /**
     * Utility service to expose account balance after a transfer request.
     * @param request
     * @param id
     * @return
     */
    @RequestMapping(value = "/accounts/{id}", method = RequestMethod.GET)
    public ResponseEntity<JSONResponseDocument> retrieveAccount(HttpServletRequest request,@PathVariable("id") long id) {

        Account account = accountService.getAccount(id);

        JSONResponseDocument jsonResponseDocument = new JSONResponseDocument<>();

        jsonResponseDocument.setData(account);

        return new ResponseEntity(jsonResponseDocument, HttpStatus.OK);
    }

}
