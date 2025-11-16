#!/bin/bash
#set -e # exit on any first error

docker compose down m1-service
docker compose down m2-service
docker compose down m3-service
docker compose down cs-api-gateway

docker rmi \
sample-mcs-m1-service:latest \
sample-mcs-m2-service:latest \
sample-mcs-m3-service:latest \
sample-mcs-cs-api-gateway:latest || true