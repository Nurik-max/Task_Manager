package org.example.Task_Manager.Controller.MVC;

import org.example.Task_Manager.Controller.security.CustomAuthenticationSuccessHandler;
import org.example.Task_Manager.DTO.workers.UpdateWorkerDTO;
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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*; // Это тоже пригодится для проверок

@WebMvcTest(WorkerController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WorkerControllerUpdateWorkerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WorkerService workerService;

    @MockBean
    WorkerRepository workerRepository;

    @MockBean
    WorkerMapper  workerMapper;

    @MockBean
    AdminTaskService adminTaskService;

    @MockBean
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;


    @Test
    void updateWorker() throws Exception {

    Worker worker = new Worker();
    worker.setUserRole(UserRole.ADMIN);
        WorkerDetails mockWorker = new WorkerDetails(worker);

        mockMvc.perform(post("/workers/{id}/edit", 1)
                        .with(user(mockWorker))// Добавили '/' в начале
                        .param("name", "Ivan")            // Рекомендую добавить параметры
                        .param("surname", "Ivanov")
                        .param("position", "Developer"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/workers")); // Добавили '/' здесь

        verify(workerService).updateWorker(eq(1), any(UpdateWorkerDTO.class));
    }
}
