package com.mk_he.Wunderlist.service;

import com.mk_he.Wunderlist.domain.entity.Task;
import com.mk_he.Wunderlist.domain.request.TaskCreateRequest;
import com.mk_he.Wunderlist.domain.request.TaskUpdateRequest;
import com.mk_he.Wunderlist.domain.response.ResponseDto;
import com.mk_he.Wunderlist.domain.response.TaskResponse;

import java.util.List;

public interface TaskService {
    ResponseDto<TaskResponse> createTask(TaskCreateRequest request);

    ResponseDto<TaskResponse> updateTask(String id, TaskUpdateRequest request);

    List<Task> getAllTasksByUsername(String username);

    ResponseDto<String> deleteTask(String id);
}
