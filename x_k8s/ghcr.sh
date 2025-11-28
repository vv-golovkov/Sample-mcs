#!/bin/bash
set -e

# NOTES: minikube was installed and these commands were executed:
## minikube start --driver=docker
## minikube addons enable ingress
## minikube addons enable metallb
## eval $(minikube docker-env) -> only for the cases with local images (not GHCR), (imagePullPolicy: Never)

# Two approaches to forward ports to reach the service outside k8s:
## $ minikube service m1-service -n sample-mcs
### (NodePort або LoadBalancer): generates random port and opens an access from outside by 127.0.0.1:<random-port>/
## minikube kubectl -- port-forward svc/cs-consul-service 8501:8500 -n sample-mcs (http://localhost:8501/ui/)
### (ClusterIP): creates a tunnel from 127.0.0.1:8501 to cs-consul-service:8500.

# minikube kubectl -- apply -f base/consul.yaml
# minikube kubectl -- apply -f base/ingress.yaml
# minikube kubectl -- port-forward svc/cs-consul-service 8501:8500 -n sample-mcs (http://localhost:8501/ui/)
# Logs:
  ## minikube kubectl -- get pods -n sample-mcs
  ## minikube kubectl -- logs -f m1-service-deployment-58fc4c4695-vg94b -n sample-mcs
# Create base64 for password: echo -n 'aBcD'|base64
# ------------------------------------------------------------------------------------
# WAS -> minikube kubectl -- get namespace sample-mcs >/dev/null 2>&1 || minikube kubectl -- create namespace sample-mcs
minikube kubectl -- apply -f base/namespace.yaml

export GHCR_TOKEN=ghp_N9v4vj82AJ7mVGrGORDzauBQkt2KWWsc3Ow4qf

minikube kubectl -- delete secret ghcr-secret -n sample-mcs --ignore-not-found

minikube kubectl -- create secret docker-registry ghcr-secret \
  --docker-server=ghcr.io \
  --docker-username=vv-golovkov \
  --docker-password=$GHCR_TOKEN \
  --namespace=sample-mcs

# how to check:
## minikube kubectl -- get ns
## minikube kubectl -- get secrets -n sample-mcs