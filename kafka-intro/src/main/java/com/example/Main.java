package com.example;

import com.example.kafka.SimpleKafkaConsumer;
import com.example.kafka.SimpleKafkaProducer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World, from Kafka-intro module :smile:");

        ExecutorService consumerExecutor = Executors.newFixedThreadPool(10);
        ExecutorService producerExecutor = Executors.newFixedThreadPool(4);

        for ( int i = 0; i < 10; i++ ) {
            consumerExecutor.submit(() -> {
                SimpleKafkaConsumer consumer = new SimpleKafkaConsumer();
                while ( true ) {
                    consumer.consume("test-topic");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
        }

        for ( int i = 0; i < 4; i++ ) {
            producerExecutor.submit(() -> {
                SimpleKafkaProducer producer = new SimpleKafkaProducer();
                int counter = 0;
                while ( true ) {
                    producer.send("test-topic", "Message #" + counter++ + " from producer thread");
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            consumerExecutor.shutdown();
            producerExecutor.shutdown();
            try {
                consumerExecutor.awaitTermination(5000, TimeUnit.MILLISECONDS);
                producerExecutor.awaitTermination(5000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }));
    }
}