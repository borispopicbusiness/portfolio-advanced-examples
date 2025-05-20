package com.example;

import com.example.kafka.SimpleKafkaConsumer;
import com.example.kafka.SimpleKafkaProducer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        SimpleKafkaProducer producer = new SimpleKafkaProducer();

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(() -> {
            try {
                int i = 0;
                while (true) {
                    producer.send("test-topic", "Hello World! " + i++);
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                System.err.println("Producer error: " + e.getMessage());
                e.printStackTrace();
            } finally {
                producer.close();
            }
        });

        SimpleKafkaConsumer consumer = new SimpleKafkaConsumer();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down...");
            consumer.close();
            producer.close();
            executorService.shutdown();
        }));

        consumer.consume();
    }
}