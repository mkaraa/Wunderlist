#!/bin/bash

set -m

# Start Couchbase Server
/entrypoint.sh couchbase-server &

# Wait for Couchbase to fully start
echo "Waiting for Couchbase Server to start..."
sleep 30

# Setup index and memory quota
echo "Setting up memory and index quotas..."
curl -v -X POST http://127.0.0.1:8091/pools/default -d memoryQuota=300 -d indexMemoryQuota=300

# Setup services
echo "Setting up services..."
curl -v http://127.0.0.1:8091/node/controller/setupServices -d services=kv%2Cn1ql%2Cindex

# Setup credentials
echo "Setting up credentials..."
curl -v http://127.0.0.1:8091/settings/web -d port=8091 -d username=mkaraa-admin -d password=12345678

# Setup Memory Optimized Indexes
echo "Setting up memory optimized indexes..."
curl -i -u mkaraa-admin:12345678 -X POST http://127.0.0.1:8091/settings/indexes -d 'storageMode=memory_optimized'

# Load travel-sample bucket
echo "Loading travel-sample bucket..."
curl -v -u mkaraa-admin:12345678 -X POST http://127.0.0.1:8091/pools/default/buckets \
    -d name=he-bucket \
    -d bucketType=couchbase \
    -d ramQuotaMB=100 \
    -d authType=sasl \
    -d replicaNumber=1

fg 1