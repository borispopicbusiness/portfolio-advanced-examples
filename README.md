# Kafka Introduction

In ./kafka-intro/docker/data run these commands before docker compose up --build

    mkdir kafka1 kafka2 kafka3
    sudo chown -R 1000:1000 ./kafka1 ./kafka2 ./kafka3
    sudo chmod -R 755 ./kafka1 ./kafka2 ./kafka3

    docker compose up --build

    docker compose down -v --remove-orphans

See all available topics in kafka1 broker run:

    kafka-topics --bootstrap-server localhost:9092 --list

Describe a topic:

    kafka-topics --bootstrap-server kafka1:9092 --describe --topic test-topic

The output looks like text below:

    Topic: test-topic	TopicId: W0T1fYREQrG5osFtl3-_0g	PartitionCount: 1	ReplicationFactor: 1	Configs: min.insync.replicas=1,segment.bytes=1073741824
	Topic: test-topic	Partition: 0	Leader: 1	Replicas: 1	Isr: 1

List all consumer groups:

    kafka-consumer-groups --bootstrap-server kafka1:9092 --list