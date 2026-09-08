package org.example.Task_Manager.Sevice.Task;

import org.example.Task_Manager.Exceptions.TaskNotFoundException;
import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Model.UserRole;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Repository.TaskRepository;
import org.example.Task_Manager.Sevice.AdminTaskService;
import org.example.Task_Manager.details.WorkerDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ATS_RestoreTaskTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private AdminTaskService adminTaskService;

    @Test
    void restoreTaskForAdmin() {

        int taskId = 1;

        Worker admin = new Worker();
        admin.setUserRole(UserRole.ADMIN);

        WorkerDetails workerDetails = new WorkerDetails(admin);

        Task entityTask = new Task();
        entityTask.setDeleted(true);
        entityTask.setDeletedAt(LocalDateTime.now());

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(entityTask));

        adminTaskService.restoreTask(taskId, workerDetails);

        assertFalse(entityTask.isDeleted());
        assertNull(entityTask.getDeletedAt());

        verify(taskRepository).findById(taskId);
        verify(taskRepository, never())
                .findByIdAndWorkerUsername(anyInt(), anyString());
    }

    @Test
    void restoreTaskForUser() {

        int taskId = 2;
        Worker user = new Worker();
        user.setUserRole(UserRole.USER);
        user.setUsername("Alan");

        WorkerDetails workerDetails = new WorkerDetails(user);

        Task entityTask = new Task();
        entityTask.setDeleted(true);
        entityTask.setDeletedAt(LocalDateTime.now());

        when(taskRepository.findByIdAndWorkerUsername(taskId, user.getUsername()))
                .thenReturn(Optional.of(entityTask));

        adminTaskService.restoreTask(taskId, workerDetails);

        assertFalse(entityTask.isDeleted());
        assertNull(entityTask.getDeletedAt());

        verify(taskRepository).findByIdAndWorkerUsername(taskId, user.getUsername());
        verify(taskRepository, never())
                .findById(taskId);
    }

    @Test
    void restore_Admin_TaskNotFound(){

        int taskId = 998;

        Worker admin = new Worker();
        admin.setUserRole(UserRole.ADMIN);
        WorkerDetails workerDetails = new WorkerDetails(admin);

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class,
                () -> adminTaskService.restoreTask(taskId, workerDetails));

        verify(taskRepository).findById(taskId);
    }

    @Test
    void restore_User_TaskNotFound(){

        int taskId = 998;

        Worker user = new Worker();
        user.setUserRole(UserRole.USER);
        user.setUsername("John");
        WorkerDetails workerDetails = new WorkerDetails(user);

        when(taskRepository.findByIdAndWorkerUsername(taskId, user.getUsername()))
                .thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class,
                () -> adminTaskService.restoreTask(taskId, workerDetails));

        verify(taskRepository).findByIdAndWorkerUsername(taskId, user.getUsername());
    }
}