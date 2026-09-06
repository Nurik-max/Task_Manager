package org.example.Task_Manager.Controller.MVC;

import org.example.Task_Manager.Controller.security.CustomAuthenticationSuccessHandler;
import org.example.Task_Manager.DTO.tasks.response.AdminResponse;
import org.example.Task_Manager.DTO.workers.response.AdminWorkerResponse;
import org.example.Task_Manager.Model.UserRole;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Repository.WorkerMapper;
import org.example.Task_Manager.Repository.WorkerRepository;
import org.example.Task_Manager.Sevice.AdminTaskService;
import org.example.Task_Manager.Sevice.WorkerService;
import org.example.Task_Manager.config.SecurityConfig;
import org.example.Task_Manager.details.WorkerDetails;
import org.example.Task_Manager.specification.WorkerSpecification;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = WorkerController.class)
@Import(SecurityConfig.class)
class WC_ShowTrashTest {


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
    void showTrash() throws Exception {

        Worker worker = new Worker();
        worker.setUserRole(UserRole.ADMIN);
        WorkerDetails workerDetails = new WorkerDetails(worker);

        Page<Worker> firedWorkers = new PageImpl<>(new ArrayList<>());

//        Page<AdminWorkerResponse> workerResponses = new PageImpl<>(new ArrayList<>());

        when(workerRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(firedWorkers);

        mockMvc.perform(get("/workers/trash")
                .with(user(workerDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("workers/trash"))
                .andExpect(model().attribute("workers", firedWorkers.map(workerMapper::toAdminWorkerResponse)));


        verify(workerRepository).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void shouldReturnForbidden() throws Exception {

        Worker worker = new Worker();
        worker.setUserRole(UserRole.USER);
        WorkerDetails workerDetails = new WorkerDetails(worker);

        mockMvc.perform(get("/workers/trash")
                .with(user(workerDetails)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(workerRepository, workerMapper);

    }
}