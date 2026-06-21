package org.example.Task_Manager.Controller;

import org.example.Task_Manager.Repoitory.WorkerRepository;
import org.example.Task_Manager.Sevice.AdminTaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerHardDeleteTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminTaskService adminTaskService;

    @MockBean
    private WorkerRepository workerRepository;

    @Test
    void hardDeleteTask() throws Exception {
        int id = 1;
        mockMvc.perform(post("/tasks/hard-delete/{id}", id)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks/trash"));
    }

    @Test
    void shouldReturn404WhenTaskNotFound() throws Exception{

        int id = 999;

//        doThrow( new TaskNotFoundException(id))
//                .when(taskService)
//                .hardDeleteTask(id);

        mockMvc.perform(post("/tasks/hard-delete/{id}", id)
                .with(csrf()))
                .andExpect(status().isNotFound());
    }
}