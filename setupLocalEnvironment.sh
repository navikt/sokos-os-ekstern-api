#!/bin/bash

# Ensure user is authenticated, and run login if not.
gcloud auth print-identity-token &> /dev/null
if [ $? -gt 0 ]; then
    gcloud auth login
fi
kubectl config use-context dev-gcp
kubectl config set-context --current --namespace=okonomi

# Get pod name
POD_NAME=$(kubectl get pods | grep sokos-os-ekstern-api | cut -f1 -d' ')

# Get AZURE system variables (excluding NAV_TRUSTSTORE_PATH)
envValue=$(kubectl exec -it $POD_NAME -c sokos-os-ekstern-api -- env | egrep "^AZURE|NAV_TRUSTSTORE" | grep -v "NAV_TRUSTSTORE_PATH" | sort)

# Extract truststore from pod to local directory
echo "Copying truststore from pod..."
kubectl cp $POD_NAME:/etc/ssl/certs/java/cacerts ./cacerts -c sokos-os-ekstern-api

# Set AZURE as local environment variables
rm -f defaults.properties
echo "$envValue" > defaults.properties

# Set truststore path to use local copy
echo "NAV_TRUSTSTORE_PATH=./cacerts" >> defaults.properties

echo "AZURE environment variables and truststore stored locally"