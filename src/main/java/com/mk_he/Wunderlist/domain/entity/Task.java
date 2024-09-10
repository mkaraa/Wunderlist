package com.mk_he.Wunderlist.domain.entity;

import com.mk_he.Wunderlist.domain.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;

import java.io.Serializable;
import java.time.LocalDate;

@Document
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    private String id;
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Title is required")
    private String title;
    private String description;
    private LocalDate dueDate;
    private TaskStatus status;

    public Task() {
    }

    public Task(String id, String username, String title, String description, LocalDate dueDate, TaskStatus status) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}

