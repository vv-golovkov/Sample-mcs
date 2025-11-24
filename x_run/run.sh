#!/bin/bash
set -e

# temporary values
export REGISTRY=local
export IMAGE_NAMESPACE=dev
export RELEASE_TAG=latest
export PROFILE=dev
export POD_NAME=pod
export GIT_COMMIT=a1b2c3

docker compose up -d cs-consul cs-vault
echo "sleeping 2s..."
sleep 2
./x_config/load-kv.sh
#docker compose up --build -d
docker compose up --build -d --scale m1-service=2 --scale m3-service=2
docker compose logs -f