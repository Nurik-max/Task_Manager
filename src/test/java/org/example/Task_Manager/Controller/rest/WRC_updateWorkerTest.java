package org.example.Task_Manager.Controller.rest;

import org.example.Task_Manager.Controller.security.CustomAuthenticationSuccessHandler;
import org.example.Task_Manager.DTO.workers.UpdateWorkerDTO;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(controllers = WorkerRestController.class)
@Import(SecurityConfig.class)
class WRC_updateWorkerTest {

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
    void update() throws Exception {

        int workerId = 1;

        Worker adminWorker = new Worker();
        adminWorker.setUserRole(UserRole.ADMIN);

        WorkerDetails workerDetails = new WorkerDetails(adminWorker);

        UpdateWorkerDTO updateWorkerDTO = new UpdateWorkerDTO();
        updateWorkerDTO.setUsername("admin");
        updateWorkerDTO.setSurname("surname");
        updateWorkerDTO.setEmail("email");
        updateWorkerDTO.setPosition("position");
        updateWorkerDTO.setPhone("0555900875");

        AdminWorkerResponse response = new AdminWorkerResponse();
        response.setUsername("admin");
        response.setSurname("surname");
        response.setEmail("email");
        response.setPosition("position");

        when(workerService.updateWorker(anyInt(), any(UpdateWorkerDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/workers/{id}/edit", workerId)
                .with(user(workerDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                        {
                                        "username": "admin",
                                        "surname": "surname",
                                        "email": "email",
                                        "position": "position",
                                        "phone": "0555900875"
                                        }
                                        """
                        ))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(updateWorkerDTO.getUsername()))
                .andExpect(jsonPath("$.surname").value(updateWorkerDTO.getSurname()))
                .andExpect(jsonPath("$.email").value(updateWorkerDTO.getEmail()))
                .andExpect(jsonPath("$.position").value(updateWorkerDTO.getPosition()));

        verify(workerService).updateWorker(eq(workerId),
                argThat(dto->
                        dto.getUsername().equals(updateWorkerDTO.getUsername())
                        && dto.getSurname().equals(updateWorkerDTO.getSurname())
                && dto.getEmail().equals(updateWorkerDTO.getEmail())
                        && dto.getPosition().equals(updateWorkerDTO.getPosition())
                        && dto.getPhone().equals(updateWorkerDTO.getPhone())
                )
        );
    }

    @Test
    void shouldReturnUnauthorized() throws Exception {

        mockMvc.perform(put("/api/workers/{id}/edit", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(workerService);

    }
}