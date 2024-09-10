package com.mk_he.Wunderlist.service.impl;

import com.mk_he.Wunderlist.config.security.SecurityContext;
import com.mk_he.Wunderlist.domain.entity.Task;
import com.mk_he.Wunderlist.domain.enums.TaskStatus;
import com.mk_he.Wunderlist.domain.mapper.CustomTaskMapper;
import com.mk_he.Wunderlist.domain.request.TaskCreateRequest;
import com.mk_he.Wunderlist.domain.request.TaskUpdateRequest;
import com.mk_he.Wunderlist.domain.response.ResponseDto;
import com.mk_he.Wunderlist.domain.response.TaskResponse;
import com.mk_he.Wunderlist.exception.TaskNotFoundException;
import com.mk_he.Wunderlist.exception.TasksFetchException;
import com.mk_he.Wunderlist.exception.UserNotFoundException;
import com.mk_he.Wunderlist.repository.TaskRepository;
import com.mk_he.Wunderlist.service.TaskService;
import com.mk_he.Wunderlist.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
    private final TaskRepository taskRepository;

    private final UserService userService;

    public TaskServiceImpl(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    @Override
    public ResponseDto<TaskResponse> createTask(TaskCreateRequest request) {
        try {
            String message = "Task created successfully";
            String username = SecurityContext.getCurrentUsername();

            Task task = CustomTaskMapper.taskCreateRequestToTask(request);
            task.setUsername(username);
            task.setStatus(TaskStatus.CONTINUED);
            Task createdTask = taskRepository.save(task);

            TaskResponse taskResponse = CustomTaskMapper.taskEntityToTaskResponse(createdTask);
            return ResponseDto.success(message, taskResponse);
        } catch (Exception e) {
            logger.error("Error creating task: {}", e.getMessage());
            return ResponseDto.failure("Error creating task: " + e.getMessage(), null);
        }
    }

    @Override
    public ResponseDto<TaskResponse> updateTask(String id, TaskUpdateRequest request) {
        try {
            String message = "Task updated successfully";
            String username = SecurityContext.getCurrentUsername();
            request.setUsername(username);

            Task task = taskRepository.findByIdAndUsername(id, username).orElseThrow(TaskNotFoundException::new);

            if (request.getTitle().isPresent() && request.getTitle().get().trim().isEmpty()) {
                message = "Title cannot be empty";
                return ResponseDto.failure(message, null);
            }

            request.getTitle().ifPresent(task::setTitle);
            request.getDescription().ifPresent(task::setDescription);
            request.getStatus().ifPresent(task::setStatus);
            request.getDueDate().ifPresent(task::setDueDate);

            Task updatedTask = taskRepository.save(task);

            TaskResponse taskResponse = CustomTaskMapper.taskEntityToTaskResponse(updatedTask);

            return ResponseDto.success(message, taskResponse);
        } catch (TaskNotFoundException e) {
            logger.error("Task not found: {}", e.getMessage());
            return ResponseDto.failure("Task not found: " + e.getMessage(), null);
        } catch (Exception e) {
            logger.error("Error updating task: {}", e.getMessage());
            return ResponseDto.failure("Error updating task: " + e.getMessage(), null);
        }
    }

    @Override
    public List<Task> getAllTasksByUsername(String username) {
        validateUser(username);

        List<Task> taskList = new ArrayList<>();
        int pageNumber = 0;
        int pageSize = 100;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Slice<Task> taskSlice;

        try {
            do {
                taskSlice = taskRepository.findAllByUsername(username, pageable);
                taskList.addAll(taskSlice.getContent());
                pageable = taskSlice.nextPageable();
            } while (taskSlice.hasNext());
        } catch (Exception e) {
            logger.error("Error fetching tasks for username '{}': {}", username, e.getMessage());
            throw new TasksFetchException(e);
        }
        return taskList;
    }

    public void validateUser(String username) {
        ResponseDto<String> isUserExists = userService.getUserByUsername(username);
        String authenticatedUser = SecurityContext.getCurrentUsername();
        if (!isUserExists.isSuccess()) {
            logger.error("Username not valid for getAllTasksByUsername {}", username);
            throw new UserNotFoundException(username);
        }
//        else if (!authenticatedUser.equals(username)) {
//            logger.error("User cannot fetch other users' tasks {}", username);
//            throw new UsersFetchDataBusinessException(username);
//        }
    }

    @Override
    public ResponseDto<String> deleteTask(String id) {
        try {
            String username = SecurityContext.getCurrentUsername();
            Task task = taskRepository.findByIdAndUsername(id, username)
                    .orElseThrow(() -> {
                        logger.error("Task with ID '{}' not found for user '{}'", id, username);
                        return new TaskNotFoundException("Task not found with ID: " + id);
                    });

            task.setStatus(TaskStatus.CANCELLED);
            taskRepository.save(task);

            logger.info("Task with ID '{}' marked as CANCELLED by user '{}'", id, username);
            return ResponseDto.success("Task deleted successfully", id);
        } catch (TaskNotFoundException e) {
            logger.error("Error deleting task: TaskNotFoundException with ID '{}': {}", id, e.getMessage(), e);
            return ResponseDto.failure("Task not found with id:", id);
        } catch (Exception e) {
            logger.error("Error deleting task with ID '{}': {}", id, e.getMessage(), e);
            return ResponseDto.failure("Error deleting task: " + e.getMessage(), null);
        }
    }

}

