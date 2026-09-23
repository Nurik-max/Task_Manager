package org.example.Task_Manager.Controller.rest;

import org.example.Task_Manager.Controller.security.CustomAuthenticationSuccessHandler;
import org.example.Task_Manager.DTO.tasks.request.AdminCreateTaskRequest;
import org.example.Task_Manager.DTO.tasks.response.AdminResponse;
import org.example.Task_Manager.Model.Priority;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(TaskRestController.class)
@Import(SecurityConfig.class)
class TRC_createTaskTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomAuthenticationSuccessHandler successHandler;

    @MockBean
    private AdminTaskService adminTaskService;

    @MockBean
    private UserTaskService userTaskService;
    @Test
    void createTask() throws Exception {

        Worker admin = new Worker();
        admin.setUserRole(UserRole.ADMIN);

        WorkerDetails workerDetails = new WorkerDetails(admin);

        AdminCreateTaskRequest taskDTO = new AdminCreateTaskRequest();
        taskDTO.setDescription("Good luck today");
        taskDTO.setPriority(Priority.MEDIUM);
        taskDTO.setWorkerId(1);

        AdminResponse adminResponse = new AdminResponse();
        adminResponse.setDescription("Good luck today");
        adminResponse.setPriority(Priority.MEDIUM);
        adminResponse.setWorkerId(1);

        when(adminTaskService.saveTask(
                any(AdminCreateTaskRequest.class),
                eq(workerDetails)
        )).thenReturn(adminResponse);

        mockMvc.perform(post("/api/tasks/admin")
                        .with(user(workerDetails))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "description": "Good luck today",
                        "priority": "MEDIUM",
                        "workerId": 1
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description").value("Good luck today"))
                .andExpect(jsonPath("$.priority").value("MEDIUM"))
                .andExpect(jsonPath("$.workerId").value(1));

        verify(adminTaskService).saveTask(
                argThat(dto ->
                        dto.getDescription().equals("Good luck today")
                                && dto.getPriority() == Priority.MEDIUM
                                && dto.getWorkerId() == 1
                ),
                eq(workerDetails)
        );
    }

    @Test
    void shouldReturnForbiddenForUser() throws Exception {

        Worker user = new Worker();
        user.setId(1);
        user.setUserRole(UserRole.USER);

        WorkerDetails workerDetails = new WorkerDetails(user);

        mockMvc.perform(post("/api/tasks/admin")
                        .with(user(workerDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
            {
                "description": "Test",
                "priority": "MEDIUM",
                "workerId": 2
            }
            """))
                .andExpect(status().isForbidden());

        verifyNoInteractions(adminTaskService);
    }

}