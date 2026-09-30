package org.example.Task_Manager.Controller.rest;

import org.example.Task_Manager.Controller.security.CustomAuthenticationSuccessHandler;
import org.example.Task_Manager.DTO.tasks.statistics.TaskStatisticsDTO;
import org.example.Task_Manager.DTO.workers.response.WorkerDTO;
import org.example.Task_Manager.Model.UserRole;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Sevice.TaskStatisticsService;
import org.example.Task_Manager.Sevice.WorkerService;
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
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(ProfileRestController.class)
@Import(SecurityConfig.class)
class PRC_getWorkerPrifileTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private  WorkerService workerService;

    @MockBean
    private  TaskStatisticsService taskStatisticsService;

    @MockBean
    private CustomAuthenticationSuccessHandler successHandler;


    @Test
    void getWorkerProfileForAdmin() throws Exception {

        Worker admin = new Worker();
        admin.setUserRole(UserRole.ADMIN);
        admin.setId(1);

        WorkerDetails workerDetails = new WorkerDetails(admin);

        WorkerDTO workerDTO = new WorkerDTO();

        TaskStatisticsDTO taskStatisticsDTO = new TaskStatisticsDTO();

        when(workerService.showWorker(workerDetails.getWorker().getId())).thenReturn(workerDTO);
        when(taskStatisticsService.getStatistics(workerDetails.getWorker().getId())).thenReturn(taskStatisticsDTO);

        mockMvc.perform(get("/api/profile")
                .with(user(workerDetails)))
                .andExpect(status().isOk());

        verify(workerService).showWorker(workerDetails.getWorker().getId());
        verify(taskStatisticsService).getStatistics(workerDetails.getWorker().getId());
    }

    @Test
    void getWorkerProfileForUser() throws Exception {

        Worker user = new Worker();
        user.setUserRole(UserRole.USER);
        user.setId(1);

        WorkerDetails workerDetails = new WorkerDetails(user);

        WorkerDTO workerDTO = new WorkerDTO();

        TaskStatisticsDTO taskStatisticsDTO = new TaskStatisticsDTO();

        when(workerService.showWorker(workerDetails.getWorker().getId())).thenReturn(workerDTO);
        when(taskStatisticsService.getStatistics(workerDetails.getWorker().getId())).thenReturn(taskStatisticsDTO);

        mockMvc.perform(get("/api/profile")
                        .with(user(workerDetails)))
                .andExpect(status().isOk());

        verify(workerService).showWorker(workerDetails.getWorker().getId());
        verify(taskStatisticsService).getStatistics(workerDetails.getWorker().getId());
    }

    @Test
    void shouldReturnUnauthorized() throws Exception {

        mockMvc.perform(get("/api/profile")
                        .with(csrf()))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(workerService, taskStatisticsService);

    }
}