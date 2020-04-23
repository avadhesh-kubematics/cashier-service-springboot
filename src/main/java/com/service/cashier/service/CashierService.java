package com.service.cashier.service;

import com.service.cashier.connector.AccountConnector;
import com.service.cashier.connector.QueueConnector;
import com.service.cashier.model.TransactionVO;
import org.springframework.stereotype.Service;

@Service
public class CashierService {

    private AccountConnector accountConnector;
    private QueueConnector queueConnector;

    public CashierService(AccountConnector accountConnector, QueueConnector queueConnector) {
        this.accountConnector = accountConnector;
        this.queueConnector = queueConnector;
    }

    public void create(TransactionVO transaction) {
        this.accountConnector.invokeAccountService(transaction.getAccountNumber());
        this.queueConnector.callQueueService(transaction);
    }
}
