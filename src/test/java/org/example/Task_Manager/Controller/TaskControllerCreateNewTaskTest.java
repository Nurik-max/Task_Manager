package org.example.Task_Manager.Controller;

import org.example.Task_Manager.Repository.WorkerRepository;
import org.example.Task_Manager.Sevice.AdminTaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerCreateNewTaskTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private AdminTaskService adminTaskService;

    @MockBean
    private WorkerRepository workerRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    void createNewTask() throws Exception {

        mockMvc.perform(post("/tasks")
                        .with(csrf())
                        .param("description", "New task")
                        .param("status", "IN_PROGRESS")
                        .param("priority", "HIGH"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks/my"));
    }
}