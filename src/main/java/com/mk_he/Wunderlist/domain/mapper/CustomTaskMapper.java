package com.mk_he.Wunderlist.domain.mapper;

import com.mk_he.Wunderlist.domain.entity.Task;
import com.mk_he.Wunderlist.domain.request.TaskCreateRequest;
import com.mk_he.Wunderlist.domain.response.TaskResponse;

public class CustomTaskMapper {
    public static Task taskCreateRequestToTask(TaskCreateRequest request) { //  OPTION: We can use BuilderPattern
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());
        return task;
    }

    public static TaskResponse taskEntityToTaskResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setTaskId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setDueDate(task.getDueDate());
        response.setStatus(task.getStatus());
        return response;
    }

    public static Task taskResponseToTaskEntity(TaskResponse response) {
        Task task = new Task();
        task.setTitle(response.getTitle());
        task.setDescription(response.getDescription());
        task.setDueDate(response.getDueDate());
        return task;
    }
}
