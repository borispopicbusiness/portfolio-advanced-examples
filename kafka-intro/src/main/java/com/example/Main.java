package com.example;

import com.example.kafka.SimpleKafkaConsumer;
import com.example.kafka.SimpleKafkaProducer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        SimpleKafkaProducer producer = new SimpleKafkaProducer();

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(
                () -> {
                    try {
                        while(true) {
                            Thread.sleep(1000);
                            producer.send("test-topic", "Hello World!");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );

        SimpleKafkaConsumer consumer = new SimpleKafkaConsumer();

        executorService.submit(() -> {
            try {
                while(true) {
                    Thread.sleep(500);
                    consumer.consume();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down...");
            consumer.close();
            producer.close();
        }));
    }
}