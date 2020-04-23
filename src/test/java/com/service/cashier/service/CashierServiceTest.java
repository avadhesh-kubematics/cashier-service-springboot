package com.service.cashier.service;

import com.service.cashier.connector.AccountConnector;
import com.service.cashier.connector.QueueConnector;
import com.service.cashier.model.TransactionVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.service.cashier.helper.TestData.getCreditTransaction;
import static org.mockito.Mockito.*;

class CashierServiceTest {

    private CashierService cashierService;
    private AccountConnector mockAccountConnector;
    private QueueConnector mockQueueConnector;

    private

    @BeforeEach
    void setUp() {
        mockAccountConnector = mock(AccountConnector.class);
        mockQueueConnector = mock(QueueConnector.class);
        cashierService = new CashierService(mockAccountConnector, mockQueueConnector);
    }

    @Test
    void create_whenTransactionDataIsPassed_shouldCallTheAccountServiceToVerify() {
        TransactionVO transaction = getCreditTransaction();

        cashierService.create(transaction);

        verify(mockAccountConnector, times(1)).
                invokeAccountService(transaction.getAccountNumber());
        verify(mockQueueConnector, times(1)).
                callQueueService(transaction);
    }
}