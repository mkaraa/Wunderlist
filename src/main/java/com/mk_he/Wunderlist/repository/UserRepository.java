package com.mk_he.Wunderlist.repository;

import com.mk_he.Wunderlist.domain.entity.User;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CouchbaseRepository<User, String> {
    Optional<User> findByUsername(String username);
}
