package com.mk_he.Wunderlist.service;

import com.mk_he.Wunderlist.domain.entity.Task;
import com.mk_he.Wunderlist.domain.enums.TaskStatus;
import com.mk_he.Wunderlist.domain.request.TaskCreateRequest;
import com.mk_he.Wunderlist.domain.request.TaskUpdateRequest;
import com.mk_he.Wunderlist.domain.response.ResponseDto;
import com.mk_he.Wunderlist.domain.response.TaskResponse;
import com.mk_he.Wunderlist.repository.TaskRepository;
import com.mk_he.Wunderlist.service.impl.TaskServiceImpl;
import com.mk_he.Wunderlist.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {
    @InjectMocks
    private TaskServiceImpl taskService;
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserServiceImpl userService;

    @Test
    @WithMockUser(username = "mkaraa", password = "123456aA")
    public void createTask_whenValidRequest_thenReturnSuccess() {
        TaskCreateRequest request = new TaskCreateRequest();
        request.setTitle("Test Task");
        request.setDescription("Test Description");
        request.setDueDate(LocalDate.now());

        Task task = new Task();
        task.setId("1");
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setDueDate(request.getDueDate());
        task.setStatus(TaskStatus.CONTINUED);
        task.setUsername("mkaraa");

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        ResponseDto<TaskResponse> response = taskService.createTask(request);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Task created successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals("1", response.getData().getTaskId());
        assertEquals("Test Task", response.getData().getTitle());
        assertEquals("Test Description", response.getData().getDescription());
        assertEquals(request.getDueDate(), response.getData().getDueDate());
        assertEquals(TaskStatus.CONTINUED, response.getData().getStatus());
    }

    @Test
    @WithMockUser(username = "mkaraa", password = "123456aA")
    public void createTask_whenSaveThrowsException_thenReturnFailure() {
        TaskCreateRequest request = new TaskCreateRequest();
        request.setTitle("Test Task");
        request.setDescription("Test Description");
        request.setDueDate(LocalDate.now());

        doThrow(new RuntimeException("Database error")).when(taskRepository).save(any(Task.class));

        ResponseDto<TaskResponse> response = taskService.createTask(request);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Error creating task: Database error", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    @WithMockUser(username = "mkaraa", password = "123456aA")
    public void updateTask_whenValidRequest_thenReturnSuccess() {
        String id = "1";
        Task existingTask = new Task();
        existingTask.setId(id);
        existingTask.setTitle("Old Title");
        existingTask.setDescription("Old Description");
        existingTask.setDueDate(LocalDate.now().minusDays(1));
        existingTask.setStatus(TaskStatus.CONTINUED);
        existingTask.setUsername("mkaraa");

        when(taskRepository.findByIdAndUsername(anyString(), anyString())).thenReturn(Optional.of(existingTask));

        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setTitle(Optional.of("Updated Title"));
        request.setDescription(Optional.of("Updated Description"));
        request.setStatus(Optional.of(TaskStatus.FINISHED));
        request.setDueDate(Optional.of(LocalDate.now()));

        Task updatedTask = new Task();
        updatedTask.setId(id);
        updatedTask.setTitle(request.getTitle().get());
        updatedTask.setDescription(request.getDescription().get());
        updatedTask.setDueDate(request.getDueDate().get());
        updatedTask.setStatus(request.getStatus().get());
        updatedTask.setUsername("mkaraa");
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        ResponseDto<TaskResponse> response = taskService.updateTask(id, request);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Task updated successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(id, response.getData().getTaskId());
        assertEquals("Updated Title", response.getData().getTitle());
        assertEquals("Updated Description", response.getData().getDescription());
        assertEquals(request.getDueDate().get(), response.getData().getDueDate());
        assertEquals(TaskStatus.FINISHED, response.getData().getStatus());
    }

    @Test
    @WithMockUser(username = "mkaraa", password = "123456aA")
    public void updateTask_whenTitleIsEmpty_thenReturnFailure() {
        String id = "1";
        Task existingTask = new Task();
        existingTask.setId(id);
        existingTask.setTitle("Old Title");
        existingTask.setDescription("Old Description");
        existingTask.setDueDate(LocalDate.now().minusDays(1));
        existingTask.setStatus(TaskStatus.CONTINUED);
        existingTask.setUsername("mkaraa");

        when(taskRepository.findByIdAndUsername(anyString(), anyString())).thenReturn(Optional.of(existingTask));

        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setTitle(Optional.of(""));
        request.setDescription(Optional.of("Updated Description"));
        request.setStatus(Optional.of(TaskStatus.FINISHED));
        request.setDueDate(Optional.of(LocalDate.now()));

        ResponseDto<TaskResponse> response = taskService.updateTask(id, request);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Title cannot be empty", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    @WithMockUser(username = "mkaraa", password = "123456aA")
    public void updateTask_whenTaskNotFound_thenReturnFailure() {
        String id = "1";
        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setTitle(Optional.of("Updated Title"));

        when(taskRepository.findByIdAndUsername(anyString(), anyString())).thenReturn(Optional.empty());

        ResponseDto<TaskResponse> response = taskService.updateTask(id, request);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Task not found: null", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    @WithMockUser(username = "mkaraa", password = "123456aA")
    public void updateTask_whenExceptionThrown_thenReturnFailure() {
        String id = "1";
        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setTitle(Optional.of("Updated Title"));

        when(taskRepository.findByIdAndUsername(anyString(), anyString())).thenThrow(new RuntimeException("Unexpected error"));

        ResponseDto<TaskResponse> response = taskService.updateTask(id, request);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Error updating task: Unexpected error", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void testValidateUser_UserExists() {
        // Arrange
        String username = "testuser";
        ResponseDto<String> response = ResponseDto.success("Username exists", username);
        when(userService.getUserByUsername(username)).thenReturn(response);

        // Act
        taskService.validateUser(username);

        // Assert

    }

    @Test
    public void getAllTasksByUsername_whenTasksExist_thenReturnTasks() {
        String username = "mkaraa";
        Task task1 = new Task();
        task1.setId("1");
        task1.setTitle("Task 1");

        Task task2 = new Task();
        task2.setId("2");
        task2.setTitle("Task 2");

        List<Task> tasksPage1 = new ArrayList<>();
        tasksPage1.add(task1);
        Slice<Task> taskSlicePage1 = new SliceImpl<>(tasksPage1, PageRequest.of(0, 100), true);

        List<Task> tasksPage2 = new ArrayList<>();
        tasksPage2.add(task2);
        Slice<Task> taskSlicePage2 = new SliceImpl<>(tasksPage2, PageRequest.of(1, 100), false);

        when(userService.getUserByUsername(eq(username)))
                .thenReturn(ResponseDto.success("Username exists", username));

        when(taskRepository.findAllByUsername(eq(username), any(Pageable.class)))
                .thenReturn(taskSlicePage1)
                .thenReturn(taskSlicePage2);

        List<Task> tasks = taskService.getAllTasksByUsername(username);

        assertNotNull(tasks);
        assertEquals(2, tasks.size());
        assertEquals("Task 1", tasks.get(0).getTitle());
        assertEquals("Task 2", tasks.get(1).getTitle());
    }

    @Test
    public void deleteTask_whenTaskExists_thenReturnSuccess() {
        String id = "1";
        String username = "Anonymous";

        Task task = new Task();
        task.setId(id);
        task.setTitle("write unit test");
        task.setStatus(TaskStatus.CONTINUED);
        task.setUsername(username);

        given(taskRepository.findByIdAndUsername("1", username)).willReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        ResponseDto<String> response = taskService.deleteTask(id);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Task deleted successfully", response.getMessage());
        assertEquals(id, response.getData());
        assertEquals(TaskStatus.CANCELLED, task.getStatus());
    }

    @Test
    public void deleteTask_whenTaskNotFound_thenReturnFailure() {
        String id = "1";
        String username = "Anonymous";

        given(taskRepository.findByIdAndUsername("1", username)).willReturn(Optional.empty());

        ResponseDto<String> response = taskService.deleteTask(id);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Task not found with id:", response.getMessage());
        assertEquals(id, response.getData());
    }

    @Test
    @WithMockUser(username = "mkaraa", password = "123456aA")
    public void deleteTask_whenExceptionThrown_thenReturnFailure() {
        String id = "1";
        String username = "mkaraa";

        when(taskRepository.findByIdAndUsername(id, username)).thenThrow(new RuntimeException("Database error"));

        ResponseDto<String> response = taskService.deleteTask(id);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertTrue(response.getMessage().contains("Error deleting task:"));
    }
}
