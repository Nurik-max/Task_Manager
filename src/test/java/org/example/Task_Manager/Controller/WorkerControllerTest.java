package org.example.Task_Manager.Controller;

import org.example.Task_Manager.Repository.WorkerRepository;
import org.example.Task_Manager.Sevice.WorkerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*; // Это тоже пригодится для проверок

@WebMvcTest(WorkerController.class)
public class WorkerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WorkerService workerService;

    @MockBean
    WorkerRepository workerRepository;

    @Test
    void updateWorker() throws Exception {
        mockMvc.perform(patch("/workers/{id}", 1) // Добавили '/' в начале
                        .param("name", "Ivan")            // Рекомендую добавить параметры
                        .param("surname", "Ivanov")
                        .param("position", "Developer"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/workers")); // Добавили '/' здесь
    }
}
