package com.service.cashier.connector;

import com.service.cashier.model.TransactionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageProducerService {

    private static final String TOPIC_NAME = "UpdatedBrandEvent";

    private final KafkaTemplate<String, TransactionVO> kafkaTemplate;

    @Autowired
    public KafkaMessageProducerService(KafkaTemplate<String, TransactionVO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(TransactionVO updatedBrandEvent) {
        kafkaTemplate.send(TOPIC_NAME, updatedBrandEvent);
    }
}