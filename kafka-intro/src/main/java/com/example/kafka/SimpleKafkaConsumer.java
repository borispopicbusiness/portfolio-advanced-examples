package com.example.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * A simple Kafka consumer that handles message consumption from specified topics.
 */
public class SimpleKafkaConsumer {
    private Properties props;
    private KafkaConsumer<String, String> consumer;

    public SimpleKafkaConsumer() {
        props = new Properties();

        props.put("bootstrap.servers","kafka1:9092,kafka2:9094,kafka3:9096");
        //props.put("group.id","my-consumer-group3");
        props.put("group.id", UUID.randomUUID().toString());
        props.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        props.put("auto.offset.reset", "earliest");

        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(List.of("test-topic"));
    }

    /**
     * Consumes messages from the specified topic.
     */
    public void consume() {
        while(true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));

            for ( ConsumerRecord<String, String> record : records ) {
                System.out.println("Received: " + record.value() + " from partition " + record.partition() + " at offset " + record.offset());
            }

            if(records.count() > 0)
                consumer.commitSync();

            try {
                Thread.sleep(2500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Closes the consumer.
     */
    public void close() {
        consumer.close();
    }
}