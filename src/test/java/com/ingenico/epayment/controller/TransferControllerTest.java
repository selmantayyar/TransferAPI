package com.ingenico.epayment.controller;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.ingenico.epayment.TransferApiApplication;
import com.ingenico.epayment.dao.AccountDao;
import com.ingenico.epayment.entity.Account;
import com.ingenico.epayment.model.json.AccountTransferInput;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TransferApiApplication.class)
@WebAppConfiguration
public class TransferControllerTest {

    private static final String ACCOUNT_NAME_TAYYAR = "TAYYAR";
    private static final String ACCOUNT_NAME_CREDITOR = "CREDITOR";
    private static final BigDecimal BALANCE_CREDITOR = BigDecimal.valueOf(100L) ;
    private static final String ACCOUNT_NAME_DEBITEUR ="DEBITEUR" ;
    private static final BigDecimal BALANCE_DEBITEUR = BigDecimal.valueOf(50L);
    private static final String ACCOUNT_NAME_OTHER_DEBITEUR ="OTHER_DEBITEUR" ;
    private static final BigDecimal BALANCE_OTHER_DEBITEUR = BigDecimal.valueOf(40L);
    private static final BigDecimal AMOUNT_TO_TRANSFER = BigDecimal.valueOf(80L);
    private static final BigDecimal AMOUNT_TO_TRANSFER_OTHER = BigDecimal.valueOf(10L);

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AccountDao accountDao;


    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

    }

    @After
    public void tearDown() throws Exception {
        accountDao.removeAll();
    }

    @Test
    public void testCreateAccount() throws Exception {
        Account account= new Account();
        account.setName(ACCOUNT_NAME_TAYYAR);
        account.setBalance(BigDecimal.valueOf(100L));

        String accountJson = json(account);

        mockMvc.perform(post("/accounts/").content(accountJson).contentType(contentType))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(contentType));
    }

    /**
     * Method creating 2 accounts,doing a transfer among and verifying final balances.
     * @throws Exception
     */
    @Test
    public void testTransfer() throws Exception {
        createAccount(ACCOUNT_NAME_CREDITOR,BALANCE_CREDITOR);
        createAccount(ACCOUNT_NAME_DEBITEUR,BALANCE_DEBITEUR);

        //fetch accounts from database.
        List<Account> accounts=accountDao.getAll();

        Account creditor= getAccountByName(accounts,ACCOUNT_NAME_CREDITOR);
        Account debiteur=getAccountByName(accounts,ACCOUNT_NAME_DEBITEUR);
        BigDecimal creditorBalanceBeforeTransfer=creditor.getBalance();
        BigDecimal debiteurBalanceBeforeTransfer=debiteur.getBalance();

        //check if accounts are created properly
        assertTrue(creditorBalanceBeforeTransfer.compareTo(BALANCE_CREDITOR)==0);
        assertTrue(debiteurBalanceBeforeTransfer.compareTo(BALANCE_DEBITEUR)==0);

        performTransfer(creditor,debiteur,AMOUNT_TO_TRANSFER);

        List<Account> accountsUpdated=accountDao.getAll();

        Account creditorUpdated= getAccountByName(accountsUpdated,ACCOUNT_NAME_CREDITOR);
        Account debiteurUpdated=getAccountByName(accountsUpdated,ACCOUNT_NAME_DEBITEUR);

        //check if balances are updated properly
        assertTrue(creditorUpdated.getBalance().compareTo(creditorBalanceBeforeTransfer.subtract(AMOUNT_TO_TRANSFER))==0);
        assertTrue(debiteurUpdated.getBalance().compareTo(debiteurBalanceBeforeTransfer.add(AMOUNT_TO_TRANSFER))==0);

    }

     /**
     * Method executing 2 concurrent transfers.And final balance should be consistent since on each request,account
      * will be locked.
     * @throws Exception
     */
    @Test
    public void testConcurrentTransfers() throws Exception {
        createAccount(ACCOUNT_NAME_CREDITOR,BALANCE_CREDITOR);
        createAccount(ACCOUNT_NAME_DEBITEUR,BALANCE_DEBITEUR);
        createAccount(ACCOUNT_NAME_OTHER_DEBITEUR,BALANCE_OTHER_DEBITEUR);

        //fetch accounts from database.
        List<Account> accounts=accountDao.getAll();

        Account creditor= getAccountByName(accounts,ACCOUNT_NAME_CREDITOR);
        Account debiteur=getAccountByName(accounts,ACCOUNT_NAME_DEBITEUR);
        Account otherDebiteur=getAccountByName(accounts,ACCOUNT_NAME_OTHER_DEBITEUR);
        BigDecimal creditorBalanceBeforeTransfer=creditor.getBalance();
        BigDecimal debiteurBalanceBeforeTransfer=debiteur.getBalance();
        BigDecimal otherDebiteurBalanceBeforeTransfer=otherDebiteur.getBalance();

        //check if accounts are created properly
        assertTrue(creditorBalanceBeforeTransfer.compareTo(BALANCE_CREDITOR)==0);
        assertTrue(debiteurBalanceBeforeTransfer.compareTo(BALANCE_DEBITEUR)==0);

        Thread t1=new Thread(()-> performTransfer(creditor,debiteur,AMOUNT_TO_TRANSFER));
        Thread t2=new Thread(()-> performTransfer(creditor,otherDebiteur,AMOUNT_TO_TRANSFER_OTHER));
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        List<Account> accountsUpdated=accountDao.getAll();

        Account creditorUpdated= getAccountByName(accountsUpdated,ACCOUNT_NAME_CREDITOR);
        Account debiteurUpdated=getAccountByName(accountsUpdated,ACCOUNT_NAME_DEBITEUR);
        Account otherDebiteurUpdated=getAccountByName(accountsUpdated,ACCOUNT_NAME_OTHER_DEBITEUR);

        //check if balances are updated properly
        BigDecimal newBalanceCreditor=creditorBalanceBeforeTransfer.subtract(AMOUNT_TO_TRANSFER).subtract
                (AMOUNT_TO_TRANSFER_OTHER);
        assertTrue(creditorUpdated.getBalance().compareTo(newBalanceCreditor)==0);
        assertTrue(debiteurUpdated.getBalance().compareTo(debiteurBalanceBeforeTransfer.add(AMOUNT_TO_TRANSFER))==0);
        assertTrue(otherDebiteurUpdated.getBalance().compareTo(otherDebiteurBalanceBeforeTransfer.add(AMOUNT_TO_TRANSFER_OTHER))==0);

    }

    private void createAccount(String name,BigDecimal balance) throws Exception {
        Account account= new Account();
        account.setName(name);
        account.setBalance(balance);

        String accountJson = json(account);

        mockMvc.perform(post("/accounts/").content(accountJson).contentType(contentType))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(contentType));
    }

    private void performTransfer(Account creditor, Account debiteur,BigDecimal amount)  {
        AccountTransferInput accountTransferInput= new AccountTransferInput();
        accountTransferInput.setAccountFromId(creditor.getId());
        accountTransferInput.setAccountToId(debiteur.getId());
        accountTransferInput.setAmount(amount);

        try {
            //Do the transfer
            String accountTransferJson = json(accountTransferInput);

            mockMvc.perform(post("/transfer/").content(accountTransferJson).contentType(contentType))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(content().contentType(contentType));
        } catch (Exception e) {
           throw new IllegalStateException(e);
        }
    }

    private Account getAccountByName(List<Account> accounts,String name) {
        return accounts.stream().filter(account->name.equals(account.getName())).findFirst
                ().get();
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
