package com.service.cashier.connector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

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
        eventMessagingConnector.produceEventMessage(getCreditTransaction());

        verify(mockKafkaTemplate, times(1))
                .send(MOCK_TOPIC_NAME, getCreditTransaction());
    }
}