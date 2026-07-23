package org.example.Task_Manager.Controller;

import org.example.Task_Manager.Controller.mvc.TaskController;
import org.example.Task_Manager.Repository.WorkerRepository;
import org.example.Task_Manager.Sevice.AdminTaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.ArgumentMatchers.any; // 🔥 Важный правильный импорт


@WebMvcTest(TaskController.class)
class TaskControllerRestoreTaskTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminTaskService adminTaskService;

    @MockBean
    private WorkerRepository workerRepository;

    @Test
    void restoreTask() throws Exception{
        int id = 1;

        mockMvc.perform(post("/tasks/restore/{id}", id)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));

//        verify(taskService).restoreTask(id);

    }
    @Test
    void shouldReturn404WhenTaskNotFound() throws Exception {

        int id = 999;

//        doThrow(new TaskNotFoundException(id))
//                .when(taskService)
//                .restoreTask(id);

        mockMvc.perform(post("/tasks/restore/{id}", id)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}