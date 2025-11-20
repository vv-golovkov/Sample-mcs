#!/bin/bash
set -e

export PROFILE=dev

docker compose up -d cs-consul cs-vault
echo "sleeping 2s..."
sleep 2
./x_config/load-kv.sh
docker compose up --build -d
docker compose logs -f