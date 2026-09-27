package org.example.Task_Manager.Controller.rest;

import org.example.Task_Manager.Controller.security.CustomAuthenticationSuccessHandler;
import org.example.Task_Manager.DTO.workers.AdminCreateWorkerDTO;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = WorkerRestController.class)
@Import(SecurityConfig.class)
class WRC_createWorkerTest {

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
    void create() throws Exception {

        Worker adminWorker = new Worker();
        adminWorker.setUserRole(UserRole.ADMIN);

        WorkerDetails workerDetails = new WorkerDetails(adminWorker);

        AdminCreateWorkerDTO adminCreateWorkerDTO = new AdminCreateWorkerDTO();
        adminCreateWorkerDTO.setUsername("admin");
        adminCreateWorkerDTO.setSurname("surname");
        adminCreateWorkerDTO.setPassword("password");
        adminCreateWorkerDTO.setEmail("email");
        adminCreateWorkerDTO.setPosition("position");

        AdminWorkerResponse  adminWorkerResponse = new AdminWorkerResponse();
        adminWorkerResponse.setUsername("admin");
        adminWorkerResponse.setSurname("surname");
        adminWorkerResponse.setEmail("email");
        adminWorkerResponse.setPassword("password");
        adminWorkerResponse.setPosition("position");

        when(workerService.createWorker(any(AdminCreateWorkerDTO.class))).thenReturn(adminWorkerResponse);

        mockMvc.perform(post("/api/workers/new")
                .with(user(workerDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                               {
                                "username": "admin",
                                "surname": "surname",
                                "password": "password",
                                "email": "email",
                                "position": "position"
                             
                              }
                              """
                ))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(adminCreateWorkerDTO.getUsername()))
                .andExpect(jsonPath("$.surname").value(adminCreateWorkerDTO.getSurname()))
                .andExpect(jsonPath("$.password").value(adminCreateWorkerDTO.getPassword()))
                .andExpect(jsonPath("$.email").value(adminCreateWorkerDTO.getEmail()))
                .andExpect(jsonPath("$.position").value(adminCreateWorkerDTO.getPosition()));

        verify(workerService).createWorker(
                argThat(dto ->
                        dto.getUsername().equals("admin")
                && dto.getSurname().equals("surname")
                        && dto.getPassword().equals("password")
                        && dto.getEmail().equals("email")
                        && dto.getPosition().equals("position")
                ));
    }

    @Test
    void shouldReturnForbiddenForUser() throws Exception {

        Worker user = new Worker();
        user.setId(1);
        user.setUserRole(UserRole.USER);

        WorkerDetails workerDetails = new WorkerDetails(user);

        mockMvc.perform(post("/api/workers/new")
                        .with(user(workerDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                       {
                                        "username": "admin",
                                        "surname": "surname",
                                        "password": "password",
                                        "email": "email",
                                        "position": "position"
                                     
                                      }
                                      """
                        ))
                .andExpect(status().isForbidden());

        verifyNoInteractions(workerService);
    }
}