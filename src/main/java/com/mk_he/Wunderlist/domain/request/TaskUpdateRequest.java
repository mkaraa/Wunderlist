package com.mk_he.Wunderlist.domain.request;

import com.mk_he.Wunderlist.domain.enums.TaskStatus;

import java.time.LocalDate;
import java.util.Optional;

public class TaskUpdateRequest {
    private String username;
    private Optional<String> title = Optional.empty();
    private Optional<String> description = Optional.empty();
    private Optional<LocalDate> dueDate = Optional.empty();
    private Optional<TaskStatus> status = Optional.empty();

    public TaskUpdateRequest() {
    }

    public TaskUpdateRequest(Optional<String> title, Optional<String> description, Optional<LocalDate> dueDate, Optional<TaskStatus> completed) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
    }

    public Optional<String> getTitle() {
        return title;
    }

    public void setTitle(Optional<String> title) {
        this.title = title;
    }

    public Optional<String> getDescription() {
        return description;
    }

    public void setDescription(Optional<String> description) {
        this.description = description;
    }

    public Optional<LocalDate> getDueDate() {
        return dueDate;
    }

    public void setDueDate(Optional<LocalDate> dueDate) {
        this.dueDate = dueDate;
    }

    public Optional<TaskStatus> getStatus() {
        return status;
    }

    public void setStatus(Optional<TaskStatus> status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
