package org.example.Task_Manager.Controller.MVC;

import org.example.Task_Manager.DTO.workers.response.AdminWorkerResponse;
import org.example.Task_Manager.Model.UserRole;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Model.WorkerStatus;
import org.example.Task_Manager.Repository.WorkerMapper;
import org.example.Task_Manager.Repository.WorkerRepository;
import org.example.Task_Manager.Sevice.AdminTaskService;
import org.example.Task_Manager.Sevice.WorkerService;
import org.example.Task_Manager.details.WorkerDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = WorkerController.class)
class WC_ListOfWorkersTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkerService workerService;

    @MockBean
    private WorkerRepository workerRepository;

    @MockBean
    private WorkerMapper workerMapper;

    @MockBean
    private AdminTaskService adminTaskService;

    @Test
    void ListOfWorkersTest() throws Exception {

        Worker worker = new Worker();
        worker.setId(1);
        worker.setUserRole(UserRole.ADMIN);
        WorkerDetails workerDetails = new WorkerDetails(worker);

        WorkerStatus workerStatus = WorkerStatus.WORKS;


        Page<AdminWorkerResponse> responses = new PageImpl<>(new ArrayList<>());

        when(workerService.getWorkers(isNull(), isNull(), isNull(), eq(workerStatus), any(Pageable.class))).thenReturn(responses);

        mockMvc.perform(get("/workers")
                        .with(user(workerDetails))
                        .param("status", workerStatus.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("workers/list"));

        verify(workerService).getWorkers(isNull(), isNull(), isNull(), eq(workerStatus), any(Pageable.class));

    }

    @Test
    void ListOfWorkersHandleInvalidFilter() throws Exception {

        Worker worker = new Worker();
        worker.setId(1);
        worker.setUserRole(UserRole.ADMIN);
        WorkerDetails workerDetails = new WorkerDetails(worker);


        Page<AdminWorkerResponse> responses = new PageImpl<>(new ArrayList<>());

        when(workerService.getWorkers(isNull(), isNull(), isNull(), isNull(),  any(Pageable.class))).thenReturn(responses);

        mockMvc.perform(get("/workers")
                        .with(user(workerDetails))
                        .param("status", "ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(view().name("workers/list"));

        verify(workerService).getWorkers(isNull(), isNull(), isNull(), isNull(), any(Pageable.class));

    }

}