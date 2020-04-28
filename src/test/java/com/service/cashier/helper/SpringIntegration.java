package com.service.cashier.helper;

import com.service.cashier.CashierApplication;
import com.service.cashier.model.TransactionVO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@SpringBootTest(
        classes = CashierApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public abstract class SpringIntegration {
    protected static final String DEFAULT_URL = "http://localhost:7093/cashier/create";

    protected RestTemplate restTemplate = new RestTemplate();

    private static String TOPIC_NAME = "transaction";

    private KafkaMessageListenerContainer<String, TransactionVO> container;

    private BlockingQueue<ConsumerRecord<String, String>> consumerRecords;

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, true, TOPIC_NAME);

    public BlockingQueue<ConsumerRecord<String, String>> setUpKafkaEmbed() {
        consumerRecords = new LinkedBlockingQueue<>();

        ContainerProperties containerProperties = new ContainerProperties(TOPIC_NAME);

        Map<String, Object> consumerProperties = KafkaTestUtils.consumerProps(
                "sender", "false", embeddedKafka.getEmbeddedKafka());

        DefaultKafkaConsumerFactory<String, TransactionVO> consumer = new DefaultKafkaConsumerFactory<>(consumerProperties);

        container = new KafkaMessageListenerContainer<>(consumer, containerProperties);
        container.setupMessageListener((MessageListener<String, String>) record -> {
            System.out.println("Listened message='{}'" + record.toString());
            consumerRecords.add(record);
        });
        container.start();

        ContainerTestUtils.waitForAssignment(container, embeddedKafka.getEmbeddedKafka().getPartitionsPerTopic());
        return consumerRecords;
    }

    public void tearDown() {
        container.stop();
    }
}
