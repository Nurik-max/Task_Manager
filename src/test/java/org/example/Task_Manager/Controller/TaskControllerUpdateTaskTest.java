package org.example.Task_Manager.Controller;

import org.example.Task_Manager.DTO.TaskDTO;
import org.example.Task_Manager.Repoitory.WorkerRepository;
import org.example.Task_Manager.Sevice.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerUpdateTaskTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private WorkerRepository workerRepository;

    @Test
    void updateTask() throws Exception {

        mockMvc.perform(patch("/tasks/1")
                        .with(csrf())
                        .param("description", "Updated task")
                        .param("status", "IN_PROGRESS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));

        verify(taskService).updateTask(eq(1), any(TaskDTO.class));
    }

    @Test
    void updateTaskValidationError() throws Exception {

        mockMvc.perform(patch("/tasks/1")
                        .with(csrf())
                        .param("description", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/edit"));
    }
}