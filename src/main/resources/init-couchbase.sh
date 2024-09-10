#!/bin/bash

set -e

COUCHBASE_HOST="localhost"
COUCHBASE_PORT="8091"
COUCHBASE_USERNAME="mkaraa-admin"
COUCHBASE_PASSWORD="12345678"

# Create buckets
couchbase-cli bucket-create \
  -c "$COUCHBASE_HOST:$COUCHBASE_PORT" \
  -u "$COUCHBASE_USERNAME" \
  -p "$COUCHBASE_PASSWORD" \
  --bucket he-bucket \
  --bucket-type couchbase \
  --bucket-ramsize 256 \
  --replica 1

echo "Bucket 'he-bucket' created."

couchbase-cli bucket-create \
  -c "$COUCHBASE_HOST:$COUCHBASE_PORT" \
  -u "$COUCHBASE_USERNAME" \
  -p "$COUCHBASE_PASSWORD" \
  --bucket he-bucket-prod \
  --bucket-type couchbase \
  --bucket-ramsize 256 \
  --replica 1

echo "Bucket 'he-bucket-prod' created."

couchbase-cli user-manage \
  -c "$COUCHBASE_HOST:$COUCHBASE_PORT" \
  -u "$COUCHBASE_USERNAME" \
  -p "$COUCHBASE_PASSWORD" \
  --set \
  --rbac-username mkaraa-admin \
  --rbac-password 12345678 \
  --roles admin

echo "Admin user 'mkaraa-admin' created."

couchbase-cli user-manage \
  -c "$COUCHBASE_HOST:$COUCHBASE_PORT" \
  -u "$COUCHBASE_USERNAME" \
  -p "$COUCHBASE_PASSWORD" \
  --set \
  --rbac-username mkaraa-user \
  --rbac-password 12345678 \
  --roles bucket_full_access[he-bucket,he-bucket-prod]

echo "Standard user 'mkaraa-user' created."