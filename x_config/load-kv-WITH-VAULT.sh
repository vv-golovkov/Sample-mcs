#!/bin/bash
set -e

#PROFILE="dev" # passed from startALL.sh
CONSUL_HOST="localhost"
CONSUL_PORT="8500"
VAULT_HOST="localhost"
VAULT_PORT="8200"
VAULT_TOKEN="root"
CONSUL_DIR="./x_config/consul"/${PROFILE}
VAULT_DIR="./x_config/vault/${PROFILE}"

until curl -s "http://${CONSUL_HOST}:${CONSUL_PORT}/v1/status/leader" | grep -q '"'; do
  echo "Consul is not ready yet. Waiting 5s..."
  sleep 3
done
echo "Consul is available"

until curl -s "http://${VAULT_HOST}:${VAULT_PORT}/v1/sys/health" | grep -q '"initialized":true'; do
  echo "Vault is not ready yet. Waiting 5s..."
  sleep 3
done
echo "Vault is available"

echo "Uploading Consul configs..." # !----- OVERRIDES whatever in CONSUL -----!
for file in "$CONSUL_DIR"/*.yaml; do
  filename=$(basename -- "$file")     # i.e.: m1-service.yaml
  basename_no_ext="${filename%.yaml}" # i.e.: m1-service
  key="config/${basename_no_ext},${PROFILE}/data" # i.e.: config/m1-service,dev/data
  echo "-> Consul: $file ($key)"
  curl -s --request PUT --data-binary @"$file" "http://${CONSUL_HOST}:${CONSUL_PORT}/v1/kv/${key}" > /dev/null
done

echo "Uploading Vault secrets..." # !----- OVERRIDES whatever in VAULT -----!
for file in "${VAULT_DIR}"/*.json; do
  service=$(basename "${file%.json}")
  key="secret/data/${service}/${PROFILE}" # secret/data/application/dev OR secret/data/m1-service/dev
  echo "-> Vault: $file ($key)"
  curl -s --header "X-Vault-Token: ${VAULT_TOKEN}" --header "Content-Type: application/json" \
      --request PUT --data @"${file}" "http://${VAULT_HOST}:${VAULT_PORT}/v1/${key}" > /dev/null
done

echo "All configs have been uploaded"