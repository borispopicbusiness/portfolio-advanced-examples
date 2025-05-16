package com.example.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * A simple Kafka producer that handles message publishing to specified topics.
 */
public class SimpleKafkaProducer {
    private final Properties props;
    private final KafkaProducer<String, String> producer;

    public SimpleKafkaProducer() {
        this.props = new Properties();
        props.put("bootstrap.servers", "kafka1:9092,kafka2:9094,kafka3:9096");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("acks", "all");

        this.producer = new KafkaProducer<>(props);
        System.out.println("Producer created with properties: " + props);
    }

    public void send(String topic, String message) {
        try {
            System.out.println("Attempting to send message: '" + message + "' to topic: " + topic);
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, message);
            
            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    System.err.println("Error sending message: " + exception.getMessage());
                    exception.printStackTrace();
                } else {
                    System.out.printf("Message sent successfully to topic=%s partition=%d offset=%d%n",
                            metadata.topic(),
                            metadata.partition(),
                            metadata.offset());
                }
            });

            producer.flush();
        } catch (Exception e) {
            System.err.println("Error in send method: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            producer.flush(); // Ensure all messages are sent before closing
            producer.close();
            System.out.println("Producer closed successfully");
        } catch (Exception e) {
            System.err.println("Error closing producer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}