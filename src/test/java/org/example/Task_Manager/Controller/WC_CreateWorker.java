package org.example.Task_Manager.Controller;

import org.example.Task_Manager.Controller.mvc.WorkerController;
import org.example.Task_Manager.DTO.workers.AdminCreateWorkerDTO;
import org.example.Task_Manager.DTO.workers.response.AdminWorkerResponse;
import org.example.Task_Manager.Model.UserRole;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Repository.WorkerMapper;
import org.example.Task_Manager.Repository.WorkerRepository;
import org.example.Task_Manager.Sevice.AdminTaskService;
import org.example.Task_Manager.Sevice.WorkerService;
import org.example.Task_Manager.details.WorkerDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WorkerController.class)
class WC_CreateWorker {

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
    void create() throws Exception {


        Worker worker = new Worker();
        worker.setId(1);
        worker.setUserRole(UserRole.ADMIN);
        WorkerDetails workerDetails = new WorkerDetails(worker);

        AdminWorkerResponse adminWorkerResponse = new AdminWorkerResponse();

        AdminCreateWorkerDTO adminCreateWorkerDTO = new AdminCreateWorkerDTO();

        when(workerService.createWorker(adminCreateWorkerDTO)).thenReturn(adminWorkerResponse);

        mockMvc.perform(post("/workers")
                        .with(csrf())
                        .with(user(workerDetails))
                        .param("username", "John")
                        .param("password", "123")
                        .param("surname", "Markovich")
                        .param("email", "john@.com")
                        .param("position", "engineer"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/workers"));

        verify(workerService).createWorker(any(AdminCreateWorkerDTO.class));
    }

    @Test
    void shouldReturnValidationErrorsWhenRequiredParametersAreMissing() throws Exception {
        Worker worker = new Worker();
        worker.setId(1);
        worker.setUserRole(UserRole.ADMIN);

        WorkerDetails workerDetails = new WorkerDetails(worker);

        AdminWorkerResponse adminWorkerResponse = new AdminWorkerResponse();

        when(workerService.createWorker(any(AdminCreateWorkerDTO.class))).thenReturn(adminWorkerResponse);

        mockMvc.perform(post("/workers")
                        .with(csrf())
                        .with(user(workerDetails))
                        .param("password", "123")
                        .param("surname", "Markovich")
                        .param("position", "engineer"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("worker", "username", "email"));;

        verify(workerService, never()).createWorker(any(AdminCreateWorkerDTO.class));
    }
}