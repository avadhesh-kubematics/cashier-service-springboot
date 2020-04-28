package com.service.cashier.connector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.ListenableFuture;

import static com.service.cashier.helper.TestData.getCreditTransaction;
import static org.mockito.Mockito.*;

class EventMessagingConnectorTest {

    public static final String MOCK_TOPIC_NAME = "mock-topicName";
    private EventMessagingConnector eventMessagingConnector;
    private KafkaTemplate mockKafkaTemplate = mock(KafkaTemplate.class);

    @BeforeEach
    void setUp() {
        eventMessagingConnector = new EventMessagingConnector(mockKafkaTemplate, MOCK_TOPIC_NAME);
    }

    @Test
    void produceEventMessage_shouldCallTheKafkaTemplate_whenTransactionVOIsPassed() {
        when(mockKafkaTemplate.send(MOCK_TOPIC_NAME, getCreditTransaction())).thenReturn(mock(ListenableFuture.class));

        eventMessagingConnector.produceEventMessage(getCreditTransaction());

        verify(mockKafkaTemplate, times(1))
                .send(MOCK_TOPIC_NAME, getCreditTransaction());
    }
}