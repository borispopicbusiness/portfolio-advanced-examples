package com.example.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.KafkaException;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

/**
 * A simple Kafka consumer that handles message consumption from specified topics.
 */
public class SimpleKafkaConsumer {
    private Properties props;
    private KafkaConsumer<String, String> consumer;

    public SimpleKafkaConsumer() {
        this.props = new Properties();

        props.put("bootstrap.servers","kafka1:9092,kafka2:9094,kafka3:9096");
        props.put("group.id","my-consumer-group2");
        props.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        props.put("auto.offset.reset","earliest");
        props.put("enable.auto.commit","true");

        this.consumer = new KafkaConsumer<>(props);
    }

    /**
     * Consumes messages from the specified topic.
     *
     * @param topic the topic to consume messages from
     * @throws KafkaException if the consume operation fails
     */
    public void consume(String topic) {
        consumer.subscribe(List.of("test-topic"));

        while(true){
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for( ConsumerRecord<String, String> record :records){
                try {
                    System.out.println("Received: " + record.value() + " from partition " + record.partition() + " at offset " + record.offset());
                    consumer.commitSync();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void close() {
        this.consumer.close();
    }
}