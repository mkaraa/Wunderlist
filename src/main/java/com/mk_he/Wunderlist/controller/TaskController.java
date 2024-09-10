package com.mk_he.Wunderlist.controller;

import com.mk_he.Wunderlist.domain.entity.Task;
import com.mk_he.Wunderlist.domain.mapper.CustomTaskMapper;
import com.mk_he.Wunderlist.domain.request.TaskCreateRequest;
import com.mk_he.Wunderlist.domain.request.TaskUpdateRequest;
import com.mk_he.Wunderlist.domain.response.ResponseDto;
import com.mk_he.Wunderlist.domain.response.TaskResponse;
import com.mk_he.Wunderlist.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/task")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<ResponseDto<TaskResponse>> createTask(@RequestBody TaskCreateRequest request) {
        ResponseDto<TaskResponse> task = taskService.createTask(request);
        return ResponseEntity.ok(task);
    }

    @PatchMapping("{taskId}")
    public ResponseEntity<ResponseDto<TaskResponse>> updateTask(@PathVariable String taskId,
                                                                @RequestBody TaskUpdateRequest request) {
        ResponseDto<TaskResponse> response = taskService.updateTask(taskId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<TaskResponse>> getAllTasksByUsername(@PathVariable String username) {
        List<Task> tasks = taskService.getAllTasksByUsername(username);
        List<TaskResponse> taskResponses = tasks.stream()
                .map(CustomTaskMapper::taskEntityToTaskResponse)
                .toList();
        return ResponseEntity.ok(taskResponses);
    }

    @DeleteMapping("{taskId}")
    public ResponseEntity<ResponseDto<String>> deleteTaskByTaskId(@PathVariable String taskId) {
        ResponseDto<String> responseDto = taskService.deleteTask(taskId);
        return ResponseEntity.ok(responseDto);
    }
}
