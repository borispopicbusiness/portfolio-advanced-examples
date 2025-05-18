#!/bin/bash

read -s -p "Enter sudo password: " SUDO_PASS
echo

echo "$SUDO_PASS" | sudo -S docker compose down -v --remove-orphans
rm --recursive --force ./data/*
mkdir ./data/kafka1 ./data/kafka2
echo "$SUDO_PASS" | sudo -S chown -R 1000:1000 ./data/*
echo "$SUDO_PASS" | sudo -S chmod -R 755 ./data/*
docker compose up --build