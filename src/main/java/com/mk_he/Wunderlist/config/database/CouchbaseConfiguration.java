package com.mk_he.Wunderlist.config.database;

import com.couchbase.client.core.error.BucketNotFoundException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.env.ClusterEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

@Configuration
@EnableCouchbaseRepositories
public class CouchbaseConfiguration extends AbstractCouchbaseConfiguration {
    private final Logger logger = LoggerFactory.getLogger(CouchbaseConfiguration.class);

    @Value("${spring.couchbase.connection-string}")
    private String connectionString;

    @Value("${spring.couchbase.username}")
    private String username;

    @Value("${spring.couchbase.password}")
    private String password;

    @Value("${spring.couchbase.env.timeouts.connect}")
    private int connectTimeout;

    @Value("${spring.data.couchbase.bucket-name}")
    private String bucketName;

    @Override
    public String getConnectionString() {
        return connectionString;
    }

    @Override
    public String getUserName() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getBucketName() {
        return bucketName;
    }

    @Override
    public String typeKey() {
        return "type";
    }

    @Override
    @Bean(destroyMethod = "disconnect")
    public Cluster couchbaseCluster(ClusterEnvironment couchbaseClusterEnvironment) {
        try {
            logger.debug("Connecting to Couchbase cluster at " + connectionString);
            return Cluster.connect(getConnectionString(), getUserName(), getPassword());
        } catch (Exception e) {
            logger.error("Error connecting to Couchbase cluster", e);
            throw e;
        }
    }

    @Bean
    public Bucket getCouchbaseBucket(Cluster cluster) {
        try {
            if (!cluster.buckets().getAllBuckets().containsKey(getBucketName())) {
                logger.error("Bucket with name {} does not exist. Creating it now", getBucketName());
                throw new BucketNotFoundException(bucketName);
            }
            return cluster.bucket(getBucketName());
        } catch (Exception e) {
            logger.error("Error getting bucket", e);
            throw e;
        }
    }
}
