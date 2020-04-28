package com.service.cashier.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.cashier.helper.SpringIntegration;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static com.service.cashier.helper.TestData.getCreditTransaction;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;
import static org.springframework.kafka.test.assertj.KafkaConditions.key;
import static org.springframework.kafka.test.hamcrest.KafkaMatchers.hasValue;


@RunWith(SpringRunner.class)
@DirtiesContext
public class EventMessagingConnectorIT extends SpringIntegration {

    @Autowired
    private EventMessagingConnector eventMessagingConnector;

    @Test
    public void it_should_send_updated_brand_event() throws InterruptedException, IOException {
        BlockingQueue<ConsumerRecord<String, String>> consumerRecords = setUpKafkaEmbed();
        eventMessagingConnector.produceEventMessage(getCreditTransaction());

        ConsumerRecord<String, String> received = consumerRecords.poll(10, TimeUnit.SECONDS);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(getCreditTransaction());

        assertThat(received, hasValue(json));

        assertThat(received).has(key(null));
        tearDown();
    }

}