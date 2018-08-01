package com.ingenico.epayment.controller;

import com.ingenico.epayment.common.TransferApiConstants;
import com.ingenico.epayment.entity.Account;
import com.ingenico.epayment.model.json.AccountErrorResponse;
import com.ingenico.epayment.model.json.AccountSuccessResponse;
import com.ingenico.epayment.model.json.JSONResponseDocument;
import com.ingenico.epayment.service.AccountService;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TransferController {

    private static final Logger LOG = LoggerFactory.getLogger(TransferController.class);

    @Autowired
    AccountService accountService;

    @RequestMapping(value = "/accounts", method = RequestMethod.POST)
    public ResponseEntity<JSONResponseDocument> createAccount(HttpServletRequest request,@RequestBody Account
            account) {
        Principal principal = request.getUserPrincipal();
        LOG.info("User {} creating account having name {} and balance {}",principal.getName(),account.getName(),
                account.getBalance());

        JSONResponseDocument jsonResponseDocument = new JSONResponseDocument<>();
        boolean validated = accountService.validateAccountData(account);
        if (!validated) {
            String strErrorMessage = TransferApiConstants.STR_ERROR_MESSAGE_ACCOUNT_VALIDATION;
            LOG.info("createAccount having name {} and balance {} rejected for User {}. Message is: {} ",account.getName(),
                    account.getBalance(),principal.getName(),strErrorMessage);

            AccountErrorResponse accountErrorResponse = new AccountErrorResponse(null,
                    TransferApiConstants.STR_WS_GENERIC_ERROR_CODE, strErrorMessage);
            jsonResponseDocument.getErrors().add(accountErrorResponse);
            return new ResponseEntity(jsonResponseDocument, HttpStatus.BAD_REQUEST);
        }

        Account accountCreated= accountService.createAccountWithBalance(account);
        AccountSuccessResponse accountSuccessResponse= new AccountSuccessResponse(TransferApiConstants
                .STR_SUCCESS_MESSAGE_ACCOUNT_CREATION,account.getId());
        jsonResponseDocument.setData(accountSuccessResponse);
        return new ResponseEntity(jsonResponseDocument, HttpStatus.CREATED);
    }

}
