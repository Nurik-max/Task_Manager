package org.example.Task_Manager.Controller.rest;

import org.example.Task_Manager.Controller.security.CustomAuthenticationSuccessHandler;
import org.example.Task_Manager.DTO.tasks.request.AdminUpdateTaskRequest;
import org.example.Task_Manager.DTO.tasks.response.AdminResponse;
import org.example.Task_Manager.Model.Priority;
import org.example.Task_Manager.Model.Status;
import org.example.Task_Manager.Model.UserRole;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Sevice.AdminTaskService;
import org.example.Task_Manager.Sevice.UserTaskService;
import org.example.Task_Manager.config.SecurityConfig;
import org.example.Task_Manager.details.WorkerDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(TaskRestController.class)
@Import(SecurityConfig.class)
class TRC_updateTaskTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomAuthenticationSuccessHandler successHandler;

    @MockBean
    private AdminTaskService adminTaskService;

    @MockBean
    private UserTaskService userTaskService;


    @Test
    void updateTask() throws Exception {

        int taskId = 1;

        Worker admin = new Worker();
        admin.setUserRole(UserRole.ADMIN);

        WorkerDetails workerDetails = new WorkerDetails(admin);

        AdminUpdateTaskRequest request = new AdminUpdateTaskRequest();
        request.setDescription("test");
        request.setStatus(Status.NEW);
        request.setPriority(Priority.HIGH);
        request.setWorkerId(11);

        AdminResponse response = new AdminResponse();
        response.setDescription("test");
        response.setStatus(Status.NEW);
        response.setPriority(Priority.HIGH);
        response.setWorkerId(11);

        when(adminTaskService.updateTask(anyInt(), any(AdminUpdateTaskRequest.class), eq(workerDetails))).thenReturn(response);

        mockMvc.perform(patch("/api/tasks/{id}/admin", taskId)
                .with(user(workerDetails))
                .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "description": "test",
                                "status": "NEW",
                                "priority": "HIGH",
                                "workerId": 11
                              
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description").value("test"))
                .andExpect(jsonPath("$.status").value("NEW"))
                .andExpect(jsonPath("$.priority").value("HIGH"));


        verify(adminTaskService).updateTask(
                eq(taskId),
                argThat(dto->
                        dto.getDescription().equals("test")
                && dto.getStatus().equals(Status.NEW)
                && dto.getPriority().equals(Priority.HIGH)),
                eq(workerDetails));
    }

    @Test
    void shouldReturnForbiddenForUser() throws Exception {

        int taskId = 1;

        Worker user = new Worker();
        user.setUserRole(UserRole.USER);

        WorkerDetails workerDetails = new WorkerDetails(user);

        AdminUpdateTaskRequest request = new AdminUpdateTaskRequest();
        request.setDescription("test");
        request.setStatus(Status.NEW);
        request.setPriority(Priority.HIGH);
        request.setWorkerId(11);

        AdminResponse response = new AdminResponse();
        response.setDescription("test");
        response.setStatus(Status.NEW);
        response.setPriority(Priority.HIGH);
        response.setWorkerId(11);

        mockMvc.perform(patch("/api/tasks/{id}/admin", taskId)
                        .with(user(workerDetails))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "description": "test",
                                "status": "NEW",
                                "priority": "HIGH",
                                "workerId": 11
                              
                                }
                                """))
                .andExpect(status().isForbidden());
        verifyNoInteractions(adminTaskService);
    }

}