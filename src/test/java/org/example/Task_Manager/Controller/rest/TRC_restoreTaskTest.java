package org.example.Task_Manager.Controller.rest;

import org.example.Task_Manager.Controller.security.CustomAuthenticationSuccessHandler;
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
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(TaskRestController.class)
@Import(SecurityConfig.class)
class TRC_restoreTaskTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomAuthenticationSuccessHandler successHandler;

    @MockBean
    private AdminTaskService adminTaskService;

    @MockBean
    private UserTaskService userTaskService;

    @Test
    void restoreTask() throws Exception {

        int taskId = 1;

        Worker admin = new Worker();
        admin.setUserRole(UserRole.ADMIN);

        WorkerDetails workerDetails = new WorkerDetails(admin);


        mockMvc.perform(patch("/api/tasks/{id}/restore", taskId)
                        .with(user(workerDetails))
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(adminTaskService).restoreTask(taskId, workerDetails);
    }

    @Test
    void restoreTaskForUser() throws Exception {
        int taskId = 1;

        Worker user = new Worker();
        user.setUserRole(UserRole.USER);

        WorkerDetails workerDetails = new WorkerDetails(user);


        mockMvc.perform(patch("/api/tasks/{id}/restore", taskId)
                        .with(user(workerDetails))
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(adminTaskService).restoreTask(taskId, workerDetails);
    }

    @Test
    void shouldReturnUnauthorized() throws Exception {

        mockMvc.perform(patch("/api/tasks/1/soft-delete")
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }
}