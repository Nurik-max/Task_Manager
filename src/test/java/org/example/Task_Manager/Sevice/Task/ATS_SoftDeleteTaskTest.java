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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ATS_SoftDeleteTaskTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private AdminTaskService adminTaskService;

    @Test
    void softDeleteTask() {

        int taskId = 1;

        Worker admin = new Worker();
        admin.setUserRole(UserRole.ADMIN);

        WorkerDetails workerDetails = new WorkerDetails(admin);

        Task entityTask = new Task();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(entityTask));

        adminTaskService.softDeleteTask(taskId, workerDetails);

        assertEquals(true, entityTask.getIsDeleted());
        assertNotNull(entityTask.getDeletedAt());
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, never()).findByIdAndWorkerUsername(anyInt(), anyString());

    }

    @Test
    void softDeleteTaskWithUser() {

        int taskId = 1;

        Worker user = new Worker();
        user.setUserRole(UserRole.USER);
        user.setUsername("John");

        WorkerDetails workerDetails = new WorkerDetails(user);

        Task entityTask = new Task();

        when(taskRepository.findByIdAndWorkerUsername(eq(taskId),eq(user.getUsername()))).thenReturn(Optional.of(entityTask));

        adminTaskService.softDeleteTask(taskId, workerDetails);

        assertEquals(true, entityTask.getIsDeleted());
        assertNotNull(entityTask.getDeletedAt());
        verify(taskRepository, times(1)).findByIdAndWorkerUsername(taskId, user.getUsername());
        verify(taskRepository, never()).findById(taskId);


    }

    @Test
    void softDeleteTask_Admin_TaskNotFound() {

        int taskId = 999;

        Worker admin = new Worker();
        admin.setUserRole(UserRole.ADMIN);

        WorkerDetails workerDetails = new WorkerDetails(admin);

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.empty());

        assertThrows(
                TaskNotFoundException.class,
                () -> adminTaskService.softDeleteTask(taskId, workerDetails)
        );

        verify(taskRepository).findById(taskId);
    }

    @Test
    void softDeleteTask_User_TaskNotFound() {

        int taskId = 999;

        Worker user = new Worker();
        user.setUserRole(UserRole.USER);
        user.setUsername("John");

        WorkerDetails workerDetails = new WorkerDetails(user);

        when(taskRepository.findByIdAndWorkerUsername(taskId, user.getUsername()))
                .thenReturn(Optional.empty());

        assertThrows(
                TaskNotFoundException.class,
                () -> adminTaskService.softDeleteTask(taskId, workerDetails)
        );

        verify(taskRepository).findByIdAndWorkerUsername(taskId,  user.getUsername());
    }
}