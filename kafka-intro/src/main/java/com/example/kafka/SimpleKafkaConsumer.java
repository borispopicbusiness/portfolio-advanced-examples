package com.example.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A simple Kafka consumer that handles message consumption from specified topics.
 */
public class SimpleKafkaConsumer {
    private final Properties props;
    private final KafkaConsumer<String, String> consumer;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private volatile Thread consumerThread;

    public SimpleKafkaConsumer() {
        props = new Properties();
        props.put("bootstrap.servers", "kafka1:9092,kafka2:9094");
        props.put("group.id", "my-consumer-group");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("auto.offset.reset", "earliest");

        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(List.of("test-topic"));
    }

    /**
     * Consumes messages from the specified topic.
     */
    public void consume() {
        consumerThread = Thread.currentThread();
        
        while (running.get()) {
            try {
                Thread.sleep(1000);
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("Received: " + record.value() + 
                        " from partition " + record.partition() + 
                        " at offset " + record.offset());
                }
                if (records.count() > 0) {
                    consumer.commitSync();
                }
            } catch (Exception e) {
                if (running.get()) {
                    System.err.println("Error during poll: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Closes the consumer safely.
     */
    public void close() {
        running.set(false);
        
        if (consumerThread != null) {
            consumerThread.interrupt();
            try {
                consumerThread.join(5000); // Wait up to 5 seconds for the thread to finish
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        try {
            consumer.close();
        } catch (Exception e) {
            System.err.println("Error closing consumer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}