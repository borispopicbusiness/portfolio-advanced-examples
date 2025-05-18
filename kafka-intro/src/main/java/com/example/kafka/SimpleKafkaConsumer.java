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
    private ConsumerRecords<String, String> records;

    public SimpleKafkaConsumer() {
        props = new Properties();

        props.put("bootstrap.servers","kafka1:9092,kafka2:9094");//,kafka3:9096
        props.put("group.id","my-consumer-group3");
        //props.put("group.id", UUID.randomUUID().toString());
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
            records = consumer.poll(Duration.ofMillis(149));

            for ( ConsumerRecord<String, String> record : records ) {
                System.out.println("Received: " + record.value() + " from partition " + record.partition() + " at offset " + record.offset());
            }

            if(records.count() > 0)
                consumer.commitSync();
        }
    }

    /**
     * Closes the consumer.
     */
    public void close() {
        if(records.count() > 0) {
            System.out.println("Committing last offsets...");
            consumer.commitSync();
            System.out.println("Last offsets committed successfully");
        }
        try {
            consumer.close();
        } catch(java.util.ConcurrentModificationException e) {
            System.out.println("Ignoring ConcurrentModificationException");
        }
    }
}