#!/bin/bash
set -e

export IMAGE_NAMESPACE=vg
export RELEASE_TAG=latest
export REGISTRY=local
export PROFILE=dev # used only when starting containers locally (not needed for images itself)

# docker compose up -d cs-consul cs-vault
# docker compose up -d cs-consul
# echo "sleeping 2s..."
# sleep 2
# ./x_config/load-kv.sh
docker compose up --build -d
#docker compose up --build -d --scale m1-service=2 --scale m3-service=2
docker compose logs -f