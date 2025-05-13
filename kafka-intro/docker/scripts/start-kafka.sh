#!/bin/bash

KAFKA_CONFIG="/etc/kafka/kafka.properties"
CLUSTER_ID="DvTS59yoTtClH_4jP3Dqzw"  # must be same across all brokers
LOG_DIR="/tmp/kraft-combined-logs"

if [ ! -f "$KAFKA_CONFIG" ]; then
  echo "Missing Kafka config at $KAFKA_CONFIG"
  exit 1
fi

# Format storage only if meta.properties doesn't exist
if [ ! -f "$LOG_DIR/meta.properties" ]; then
  echo "Formatting storage with cluster ID: $CLUSTER_ID"
  kafka-storage format -t "$CLUSTER_ID" -c "$KAFKA_CONFIG"
fi

# Start Kafka
exec kafka-server-start "$KAFKA_CONFIG"