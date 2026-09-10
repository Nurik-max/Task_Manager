package org.example.Task_Manager.Sevice;

import org.example.Task_Manager.DTO.tasks.response.AdminResponse;
import org.example.Task_Manager.Model.*;
import org.example.Task_Manager.Repository.TaskMapper;
import org.example.Task_Manager.Repository.TaskRepository;
import org.example.Task_Manager.details.WorkerDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ATS_getDeletedTasksTest {


    @InjectMocks
    private AdminTaskService adminTaskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Test
    void getDeletedTasks() {

        Worker worker = new Worker();
        worker.setUserRole(UserRole.ADMIN);

        WorkerDetails workerDetails = new WorkerDetails(worker);

        Pageable pageable = PageRequest.of(0, 10);

        Task task = new Task();
        task.setDeleted(true);

        AdminResponse adminResponse = new AdminResponse();

        Page<Task> taskPage = new PageImpl<>(List.of(task));

        when(taskRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(taskPage);

        when(taskMapper.toAdminResponse(task)).thenReturn(adminResponse);

        Page<AdminResponse> result = adminTaskService.getDeletedTasks(null, null, null, null, null, true, pageable,  workerDetails);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(adminResponse, result.getContent().get(0));
        verify(taskRepository).findAll( any(Specification.class), eq(pageable));
        verify(taskMapper).toAdminResponse(task);


    }

    @Test
    void shouldReturnEmptyListWhenNoDeletedTasks() {

        Worker worker = new Worker();
        worker.setUserRole(UserRole.ADMIN);

        WorkerDetails workerDetails = new WorkerDetails(worker);

        Pageable pageable = PageRequest.of(0, 10);

        Task task = new Task();
        task.setDeleted(true);

        AdminResponse adminResponse = new AdminResponse();

        Page<Task> taskPage = Page.empty(pageable);

        when(taskRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(taskPage);

        Page<AdminResponse> result = adminTaskService.getDeletedTasks(null, null, null, null, null, true, pageable, workerDetails);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.getContent().size());
        verify(taskRepository).findAll( any(Specification.class), eq(pageable));
       verifyNoInteractions(taskMapper);

    }

    @Test
    void getDeletedTasksWithFilters() {

        Worker worker = new Worker();
        worker.setUserRole(UserRole.ADMIN);

        WorkerDetails workerDetails = new WorkerDetails(worker);

        Pageable pageable = PageRequest.of(0, 10);

        Task task = new Task();
        task.setDeleted(true);
        task.setStatus(Status.NEW);
        task.setPriority(Priority.HIGH);
        task.setDescription("Description");


        AdminResponse adminResponse = new AdminResponse();
        adminResponse.setStatus(Status.NEW);
        adminResponse.setPriority(Priority.HIGH);
        adminResponse.setDescription("Description");

        Page<Task> taskPage = new PageImpl<>(List.of(task));

        when(taskRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(taskPage);

        when(taskMapper.toAdminResponse(task)).thenReturn(adminResponse);

        Page<AdminResponse> result = adminTaskService.getDeletedTasks(task.getStatus(), task.getPriority(), task.getDescription(), null, null, task.isDeleted(), pageable,  workerDetails);

        assertNotNull(result);
        assertEquals(Status.NEW, result.getContent().get(0).getStatus());
        assertEquals(Priority.HIGH, result.getContent().get(0).getPriority());
        assertEquals("Description", result.getContent().get(0).getDescription());
        assertEquals(1, result.getContent().size());
        assertEquals(adminResponse, result.getContent().get(0));
        verify(taskRepository).findAll( any(Specification.class), eq(pageable));
        verify(taskMapper).toAdminResponse(task);


    }
}