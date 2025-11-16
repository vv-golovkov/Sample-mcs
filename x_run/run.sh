#!/bin/bash
set -e

export PROFILE=dev

docker compose up -d cs-consul
echo "sleeping 2s..."
sleep 2
./x_consul/load-kv.sh
docker compose up --build -d
docker compose logs -f