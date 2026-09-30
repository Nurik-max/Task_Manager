package org.example.Task_Manager.Controller.rest;

import org.example.Task_Manager.Controller.security.CustomAuthenticationSuccessHandler;
import org.example.Task_Manager.DTO.workers.response.AdminWorkerResponse;
import org.example.Task_Manager.Model.UserRole;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Model.WorkerStatus;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@WebMvcTest(controllers = WorkerRestController.class)
@Import(SecurityConfig.class)
class WRC_showTrashTest {

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
    void showTrash() throws Exception {

        Worker worker = new Worker();
        worker.setUserRole(UserRole.ADMIN);
        WorkerDetails workerDetails = new WorkerDetails(worker);

        Worker worker1 = new Worker();
        worker1.setId(1);
        worker1.setUserRole(UserRole.USER);
        worker1.setWorkerStatus(WorkerStatus.FIRED);

        AdminWorkerResponse response = new AdminWorkerResponse();
        Page<Worker> firedWorkers = new PageImpl<>(List.of(worker1));

        when(workerRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(firedWorkers);
        when(workerMapper.toAdminWorkerResponse(worker1)).thenReturn(response);

        mockMvc.perform(get("/api/workers/trash")
                .with(user(workerDetails)))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.content[0]").exists());

        verify(workerRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(workerMapper).toAdminWorkerResponse(worker1);
    }


    @Test
    void shouldReturnForbiddenForUser() throws Exception {

        Worker user = new Worker();
        user.setId(1);
        user.setUserRole(UserRole.USER);

        WorkerDetails workerDetails = new WorkerDetails(user);

        mockMvc.perform(get("/api/workers/trash")
                        .with(user(workerDetails)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(workerRepository, workerMapper);
    }
}