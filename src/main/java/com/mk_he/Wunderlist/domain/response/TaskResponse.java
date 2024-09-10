package com.mk_he.Wunderlist.domain.response;

import com.mk_he.Wunderlist.domain.enums.TaskStatus;

import java.time.LocalDate;

public class TaskResponse {
    private String taskId;
    private String title;
    private String description;
    private LocalDate dueDate;
    private TaskStatus status;

    public TaskResponse() {
    }

    public TaskResponse(String taskId, String title, String description, LocalDate dueDate, TaskStatus status) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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
