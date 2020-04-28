package com.service.cashier.service;

import com.service.cashier.connector.AccountConnector;
import com.service.cashier.connector.EventMessagingConnector;
import com.service.cashier.model.TransactionVO;
import org.springframework.stereotype.Service;

@Service
public class CashierService {

    private AccountConnector accountConnector;
    private EventMessagingConnector eventMessagingConnector;

    public CashierService(AccountConnector accountConnector, EventMessagingConnector eventMessagingConnector) {
        this.accountConnector = accountConnector;
        this.eventMessagingConnector = eventMessagingConnector;
    }

    public void create(TransactionVO transaction) {
        this.accountConnector.invokeAccountService(transaction.getAccountNumber());
        this.eventMessagingConnector.produceEventMessage(transaction);
    }
}
