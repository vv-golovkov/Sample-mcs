#!/bin/bash
set -e

#export CONSUL_PORT=8500
#export CONSUL_HOST=localhost
#export CONFIG_DIR=../x_consul/config

CONSUL_HOST="localhost"
CONSUL_PORT="8500"

# PROFILE=${1:-"dev"} # ./run.sh dev OR ./run.sh prod
# CONFIG_DIR="../x_consul/config"/${PROFILE}
#PROFILE="dev"
CONFIG_DIR="./x_consul/config"/${PROFILE} # PROFILE is passed from startALL.sh

until curl -s "http://${CONSUL_HOST}:${CONSUL_PORT}/v1/status/leader" | grep -q '"'; do
#until nc -z "${CONSUL_HOST}" "${CONSUL_PORT}" >/dev/null 2>&1; do
  echo "Consul is not ready yet. Waiting 5s..."
  sleep 5 # another way to sleep (read -t 5 < /dev/null || true)
done
echo "Consul is available"

echo "Loading files from '$CONFIG_DIR' to Consul ..."

shopt -s nullglob
for file in "$CONFIG_DIR"/*.yaml; do # !----- OVERRIDES whatever in consul -----!
  filename=$(basename -- "$file")     # i.e.: m1-service.yaml
  basename_no_ext="${filename%.yaml}" # i.e.: m1-service
  key="config/${basename_no_ext},${PROFILE}/data" # i.e.: config/m1-service,dev/data
  echo " Put file $file to Consul - $key"
  curl -s --request PUT --data-binary @"$file" "http://${CONSUL_HOST}:${CONSUL_PORT}/v1/kv/${key}"
  # curl --request PUT --data-binary @"$file" "http://${CONSUL_HOST}:${CONSUL_PORT}/v1/kv/$key"
done

echo "All configs have been loaded to Consul"
