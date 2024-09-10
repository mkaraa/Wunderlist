package com.mk_he.Wunderlist.repository;

import com.mk_he.Wunderlist.domain.entity.Task;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface TaskRepository extends CouchbaseRepository<Task, String> {
    Optional<Task> findByIdAndUsername(String id, String username);

    Slice<Task> findAllByUsername(String username, Pageable pageable);
}
