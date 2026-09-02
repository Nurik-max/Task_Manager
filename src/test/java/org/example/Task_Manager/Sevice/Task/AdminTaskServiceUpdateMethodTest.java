package org.example.Task_Manager.Sevice.Task;

import org.example.Task_Manager.DTO.tasks.TaskDTO;
import org.example.Task_Manager.DTO.tasks.request.AdminCreateTaskRequest;
import org.example.Task_Manager.DTO.tasks.request.AdminUpdateTaskRequest;
import org.example.Task_Manager.DTO.tasks.request.CreateTaskRequest;
import org.example.Task_Manager.DTO.tasks.response.AdminResponse;
import org.example.Task_Manager.Model.Priority;
import org.example.Task_Manager.Model.Status;
import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Repository.TaskMapper;
import org.example.Task_Manager.Repository.TaskRepository;
import org.example.Task_Manager.Sevice.AdminTaskService;
import org.example.Task_Manager.details.WorkerDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminTaskServiceUpdateMethodTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private AdminTaskService adminTaskService;

    @Test
    void shouldUpdateTask() {
        // arrange
        int taskId = 1;

        AdminUpdateTaskRequest request = new AdminUpdateTaskRequest();
        request.setDescription("Updated Description");
        request.setStatus(Status.DONE);
        request.setPriority(Priority.HIGH);

        Task existingTask = new Task();
        existingTask.setId(taskId);

        AdminResponse expectedResponse = new AdminResponse();
        expectedResponse.setDescription("Updated Description");
        expectedResponse.setStatus(Status.DONE);
        expectedResponse.setPriority(Priority.HIGH);

        Worker worker = new Worker();
        WorkerDetails workerDetails = new WorkerDetails(worker);

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(existingTask));

        when(taskMapper.toAdminResponse(existingTask))
                .thenReturn(expectedResponse);

        // act
        AdminResponse actualResponse =
                adminTaskService.updateTask(taskId, request, workerDetails);

        // assert
        assertNotNull(actualResponse);
        assertEquals("Updated Description", actualResponse.getDescription());
        assertEquals(Status.DONE, actualResponse.getStatus());
        assertEquals(Priority.HIGH, actualResponse.getPriority());
        assertSame(expectedResponse, actualResponse);

        // verify
        verify(taskRepository).findById(taskId);
        verify(taskMapper).updateTaskForAdmin(existingTask, request);
        verify(taskMapper).toAdminResponse(existingTask);
    }
}