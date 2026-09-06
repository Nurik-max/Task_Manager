package org.example.Task_Manager.Controller.MVC;

import org.example.Task_Manager.Controller.security.CustomAuthenticationSuccessHandler;
import org.example.Task_Manager.DTO.workers.response.AdminWorkerResponse;
import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Model.UserRole;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Model.WorkerStatus;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(controllers = WorkerController.class)
@Import(SecurityConfig.class)
class WC_HardDeleteWorkerTest {

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
    void hardDeleteWorker() throws Exception {

        int workerId = 2;
      Integer newWorkerId = 3;

        Worker admin = new Worker();
        admin.setId(1);
        admin.setUserRole(UserRole.ADMIN);

        WorkerDetails workerDetails = new WorkerDetails(admin);

        AdminWorkerResponse workerResponse = new AdminWorkerResponse();

        List<Task> tasks = new ArrayList<>();

        when(adminTaskService.getTasksByWorkerId(workerId)).thenReturn(tasks);
        when(workerService.hardDeleteWorker(workerId, newWorkerId)).thenReturn(workerResponse);

        mockMvc.perform(post("/workers/{id}/force-delete", workerId)
                        .param("newWorkerId", newWorkerId.toString())
                        .with(csrf())
                .with(user(workerDetails)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/workers/trash"));

        verify(adminTaskService).getTasksByWorkerId(workerId);
        verify(workerService).hardDeleteWorker(workerId, newWorkerId);

    }

    @Test
    void shouldHardDeleteWhenWorkerHasTasksAndNewWorkerProvided() throws Exception{

        int workerId = 2;
        Integer newWorkerId = 3;

        Worker admin = new Worker();
        admin.setId(1);
        admin.setUserRole(UserRole.ADMIN);

        WorkerDetails workerDetails = new WorkerDetails(admin);

//        AdminWorkerResponse workerResponse = new AdminWorkerResponse();

        List<Task> tasks = List.of(new Task());

        when(adminTaskService.getTasksByWorkerId(workerId)).thenReturn(tasks);
        when(workerService.hardDeleteWorker(workerId, newWorkerId)).thenReturn(null);

        mockMvc.perform(post("/workers/{id}/force-delete", workerId)
                        .param("newWorkerId", newWorkerId.toString())
                        .with(csrf())
                        .with(user(workerDetails)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/workers/trash"));

        verify(adminTaskService).getTasksByWorkerId(workerId);
        verify(workerService).hardDeleteWorker(workerId, newWorkerId);
    }

    @Test
    void shouldRedirectToEditWhenWorkerHasTasks() throws Exception {

        int workerId = 2;

        Worker admin = new Worker();
        admin.setId(1);
        admin.setUserRole(UserRole.ADMIN);

        WorkerDetails workerDetails = new WorkerDetails(admin);

        List<Task> tasks = List.of(new Task());

        when(adminTaskService.getTasksByWorkerId(workerId)).thenReturn(tasks);

        mockMvc.perform(post("/workers/{id}/force-delete", workerId)
                        .with(csrf())
                        .with(user(workerDetails)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(  "/workers/" + workerId + "/edit?error=has_tasks"));


        verify(adminTaskService).getTasksByWorkerId(workerId);
        verifyNoInteractions(workerService);
    }

    @Test
    void shouldReturnForbiddenForUser() throws Exception {

        int workerId = 2;

        Worker user = new Worker();
        user.setId(1);
        user.setUserRole(UserRole.USER);

        WorkerDetails workerDetails = new WorkerDetails(user);

        mockMvc.perform(post("/workers/{id}/force-delete", workerId)
                        .with(csrf())
                        .with(user(workerDetails)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(workerService, adminTaskService);

    }

    }