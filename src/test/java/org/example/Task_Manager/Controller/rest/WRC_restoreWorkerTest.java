package org.example.Task_Manager.Controller.rest;

import org.example.Task_Manager.Controller.security.CustomAuthenticationSuccessHandler;
import org.example.Task_Manager.DTO.workers.response.AdminWorkerResponse;
import org.example.Task_Manager.Model.UserRole;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Repository.WorkerMapper;
import org.example.Task_Manager.Repository.WorkerRepository;
import org.example.Task_Manager.Sevice.WorkerService;
import org.example.Task_Manager.config.SecurityConfig;
import org.example.Task_Manager.details.WorkerDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(controllers = WorkerRestController.class)
@Import(SecurityConfig.class)
class WRC_restoreWorkerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkerService workerService;

    @MockBean
    private WorkerRepository workerRepository;

    @MockBean
    private WorkerMapper workerMapper;

    @MockBean
    private CustomAuthenticationSuccessHandler successHandler;

    @Test
    void restoreWorker() throws  Exception{

        Worker admin   = new Worker();
        admin.setUserRole(UserRole.ADMIN);

        WorkerDetails workerDetails = new WorkerDetails(admin);

        int workerId = 1;

        AdminWorkerResponse response = new AdminWorkerResponse();

        when(workerService.restoreWorker(eq(workerId))).thenReturn(response);

        mockMvc.perform(post("/api/workers/{id}/restore", workerId)
                        .with(user(workerDetails))
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(workerService, times(1)).restoreWorker(eq(workerId));
    }

    @Test
    void shouldReturnForbiddenForUser() throws Exception {

        Worker user = new Worker();
        user.setId(1);
        user.setUserRole(UserRole.USER);

        WorkerDetails workerDetails = new WorkerDetails(user);

        mockMvc.perform(post("/api/workers/{id}/restore", 1)
                        .with(user(workerDetails))
                        .with(csrf()))
                .andExpect(status().isForbidden());

        verifyNoInteractions(workerService);
    }
}