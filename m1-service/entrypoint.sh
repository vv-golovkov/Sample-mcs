#!/bin/sh
set -e

# ----------------------------------------
# Settings
# ----------------------------------------
APP_NAME="m1-service"
PROFILE="${SPRING_PROFILES_ACTIVE}"
CONSUL_HOST="${SPRING_CLOUD_CONSUL_HOST}"
CONSUL_PORT="${SPRING_CLOUD_CONSUL_PORT}"
CONSUL_KEY="config/${APP_NAME},${PROFILE}/data"
LOCAL_FILE="application-${PROFILE}.yaml"

# ----------------------------------------
# Simple color helpers
# ----------------------------------------
GREEN="\033[0;32m"; YELLOW="\033[1;33m"; RED="\033[0;31m"; RESET="\033[0m"
log() { echo "${GREEN}[$(date '+%H:%M:%S')]${RESET} $1"; }
warn() { echo "${YELLOW}[$(date '+%H:%M:%S')] $1${RESET}"; }
error() { echo "${RED}[$(date '+%H:%M:%S')] $1${RESET}"; }

# ----------------------------------------
# Wait for Consul
# ----------------------------------------
log "Waiting for Consul at ${CONSUL_HOST}:${CONSUL_PORT} ..."
until nc -z "${CONSUL_HOST}" "${CONSUL_PORT}" >/dev/null 2>&1; do
  warn "Consul not ready yet..."
  sleep 2
done
log "Consul is available"

# ----------------------------------------
# Check if config already exists
# ----------------------------------------
if consul kv get -http-addr="http://${CONSUL_HOST}:${CONSUL_PORT}" "${CONSUL_KEY}" >/dev/null 2>&1; then
  log "Config already exists in Consul KV: ${CONSUL_KEY}"
else
  if [ -f "${LOCAL_FILE}" ]; then
    log "Uploading local ${LOCAL_FILE} → ${CONSUL_KEY}"
    consul kv put -http-addr="http://${CONSUL_HOST}:${CONSUL_PORT}" "${CONSUL_KEY}" @"${LOCAL_FILE}"
    log "Config uploaded successfully"
  else
    warn "Local file ${LOCAL_FILE} not found — skipping upload"
  fi
fi

# ----------------------------------------
# Start app
# ----------------------------------------
log "Starting ${APP_NAME} with profile=${PROFILE}"
exec java -jar app.jar