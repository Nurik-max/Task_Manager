package org.example.Task_Manager.Controller.rest;

import org.example.Task_Manager.Controller.security.CustomAuthenticationSuccessHandler;
import org.example.Task_Manager.DTO.workers.response.AdminWorkerResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(controllers = WorkerRestController.class)
@Import(SecurityConfig.class)
class WRC_listOfWorkers {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private  WorkerService workerService;

    @MockBean
    private  WorkerRepository workerRepository;

    @MockBean
    private  WorkerMapper workerMapper;

    @MockBean
    private CustomAuthenticationSuccessHandler successHandler;


    @Test
    void listWorkers() throws Exception {

        Worker admin = new Worker();
        admin.setUserRole(UserRole.ADMIN);

        WorkerDetails adminDetails = new WorkerDetails(admin);

        Pageable pageable = PageRequest.of(0, 10);

        AdminWorkerResponse  adminResponse = new AdminWorkerResponse();
        adminResponse.setId(1);
        adminResponse.setUsername("admin");
        adminResponse.setPassword("1234");
        adminResponse.setEmail("admin@.com");

        Page<AdminWorkerResponse> adminResponsePage = new PageImpl<>(List.of(adminResponse), pageable,1);

        when(workerService.getWorkers(null, null, null, null, pageable)).thenReturn(adminResponsePage);

        mockMvc.perform(get("/api/workers/worker-list")
                .with(user(adminDetails)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].username").value("admin"))
                .andExpect(jsonPath("$.content[0].password").value("1234"))
                .andExpect(jsonPath("$.content[0].email").value("admin@.com"));

        verify(workerService).getWorkers(null, null, null, null, pageable);

    }

@Test
    void listWorkersWithFilters() throws Exception {

        Worker admin = new Worker();
        admin.setUserRole(UserRole.ADMIN);

        WorkerDetails adminDetails = new WorkerDetails(admin);

        Pageable pageable = PageRequest.of(2, 10);

        Page<AdminWorkerResponse> adminResponsePage = new PageImpl<>(List.of(), pageable,1);

        when(workerService.getWorkers("Nurik", "Gojo", "developer", WorkerStatus.WORKS, pageable)).thenReturn(adminResponsePage);

        mockMvc.perform(get("/api/workers/worker-list")
                .with(user(adminDetails))
                        .param("page", "2")
                .param("username", "Nurik")
                .param("surname", "Gojo")
                .param("position", "developer")
                .param("status", "WORKS"))
                .andExpect(status().isOk());

        verify(workerService).getWorkers("Nurik", "Gojo", "developer", WorkerStatus.WORKS, pageable);

    }

    @Test  //test for checking try-cath exception
    void listWorkersWithInvalidStatus() throws Exception {

        Worker admin = new Worker();
        admin.setUserRole(UserRole.ADMIN);

        WorkerDetails adminDetails = new WorkerDetails(admin);

        Pageable pageable = PageRequest.of(0, 10);

        Page<AdminWorkerResponse> page =
                new PageImpl<>(List.of(), pageable, 0);

        when(workerService.getWorkers(
                null,
                null,
                null,
                null,
                pageable
        )).thenReturn(page);

        mockMvc.perform(get("/api/workers/worker-list")
                        .with(user(adminDetails))
                        .param("status", "INVALID"))
                .andExpect(status().isOk());

        verify(workerService).getWorkers(
                null,
                null,
                null,
                null,
                pageable
        );
    }

    @Test
    void getAllTasks_forUser_forbidden() throws Exception {

        Worker user = new Worker();
        user.setUserRole(UserRole.USER);
        user.setId(2);

        WorkerDetails userDetails = new WorkerDetails(user);

        mockMvc.perform(get("/api/workers/worker-list")
                        .with(user(userDetails)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(workerService);
    }
}