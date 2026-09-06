package org.example.Task_Manager.Controller.MVC;

import org.example.Task_Manager.Controller.security.CustomAuthenticationSuccessHandler;
import org.example.Task_Manager.DTO.workers.response.AdminWorkerResponse;
import org.example.Task_Manager.Model.UserRole;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Repository.WorkerMapper;
import org.example.Task_Manager.Repository.WorkerRepository;
import org.example.Task_Manager.Sevice.AdminTaskService;
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
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(controllers = WorkerController.class)
@Import(SecurityConfig.class)
class WC_RestoreWorkerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomAuthenticationSuccessHandler successHandler;

    @MockBean
    private WorkerService workerService;

    @MockBean
    private AdminTaskService adminTaskService;

    @MockBean
    private WorkerMapper workerMapper;

    @MockBean
    private WorkerRepository workerRepository;

    @Test
    void restoreWorker() throws Exception {

        int workerId = 1;

        Worker admin = new Worker();
        admin.setId(2);
        admin.setUserRole(UserRole.ADMIN);

        WorkerDetails workerDetails = new WorkerDetails(admin);

        AdminWorkerResponse adminWorkerResponse = new AdminWorkerResponse();

        when(workerService.restoreWorker(workerId)).thenReturn(adminWorkerResponse);

        mockMvc.perform(post("/workers/{id}/restore", workerId)
                        .with(csrf())
                .with(user(workerDetails)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/workers"));

        verify(workerService).restoreWorker(workerId);

    }

    @Test
    void shouldForbiddenForUser() throws Exception {

        int workerId = 1;
        Worker user = new Worker();
        user.setId(2);
        user.setUserRole(UserRole.USER);

        WorkerDetails workerDetails = new WorkerDetails(user);

        mockMvc.perform(post("/workers/{id}/restore", workerId)
                        .with(csrf())
                        .with(user(workerDetails)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(workerService);
    }
}