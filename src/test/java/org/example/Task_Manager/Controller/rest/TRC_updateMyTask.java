package org.example.Task_Manager.Controller.rest;

import org.example.Task_Manager.Controller.security.CustomAuthenticationSuccessHandler;
import org.example.Task_Manager.DTO.tasks.request.AdminUpdateTaskRequest;
import org.example.Task_Manager.DTO.tasks.request.UpdateTaskRequest;
import org.example.Task_Manager.DTO.tasks.response.AdminResponse;
import org.example.Task_Manager.DTO.tasks.response.UserResponse;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(TaskRestController.class)
@Import(SecurityConfig.class)
class TRC_updateMyTask {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomAuthenticationSuccessHandler successHandler;

    @MockBean
    private AdminTaskService adminTaskService;

    @MockBean
    private UserTaskService userTaskService;

    @Test
    void updateMyTask() throws Exception {

        int taskId = 1;

        Worker user = new Worker();
        user.setUserRole(UserRole.USER);

        WorkerDetails workerDetails = new WorkerDetails(user);

        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setDescription("test");
        request.setStatus(Status.NEW);
        request.setPriority(Priority.HIGH);


        UserResponse response = new UserResponse();
        response.setDescription("test");
        response.setStatus(Status.NEW);
        response.setPriority(Priority.HIGH);


        when(userTaskService.updateUserTask(anyInt(), any(UpdateTaskRequest.class), eq(workerDetails))).thenReturn(response);

        mockMvc.perform(patch("/api/tasks/{id}/user", taskId)
                .with(user(workerDetails))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                {
                                "description": "test",
                                "status": "NEW",
                                "priority": "HIGH"
                              
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("test"))
                .andExpect(jsonPath("$.status").value("NEW"))
                .andExpect(jsonPath("$.priority").value("HIGH"));

        verify(userTaskService).updateUserTask(
                eq(taskId),
                argThat(dto->
                        dto.getDescription().equals("test")
                                && dto.getStatus().equals(Status.NEW)
                                && dto.getPriority().equals(Priority.HIGH)),
                eq(workerDetails));
    }

    @Test
    void shouldReturnUnauthorized() throws Exception {


        mockMvc.perform(patch("/api/tasks/1/user")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))

                .andExpect(status().isUnauthorized());

        verifyNoInteractions(userTaskService);

    }
}