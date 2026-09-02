package org.example.Task_Manager.Controller;

import org.example.Task_Manager.Controller.mvc.TaskController;
import org.example.Task_Manager.Exceptions.TaskNotFoundException;
import org.example.Task_Manager.Model.UserRole;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Repository.WorkerRepository;
import org.example.Task_Manager.Sevice.AdminTaskService;
import org.example.Task_Manager.Sevice.UserTaskService;
import org.example.Task_Manager.details.WorkerDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.ArgumentMatchers.any; // 🔥 Важный правильный импорт

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@WebMvcTest(TaskController.class)
class TaskControllerRestoreTaskTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminTaskService adminTaskService;

    @MockBean
    private UserTaskService userTaskService;

    @MockBean
    private WorkerRepository workerRepository;

    @Test
    void restoreTask() throws Exception{
        int id = 1;

        Worker mockWorker = new Worker();
        mockWorker.setUserRole(UserRole.USER);
        WorkerDetails mockWorkerDetails = new WorkerDetails(mockWorker);

        mockMvc.perform(post("/tasks/restore/{id}", id)
                        .with(user(mockWorkerDetails))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks/my"));

        verify(adminTaskService).restoreTask(eq(id), any());

    }
    @Test
    @WithMockUser
    void shouldReturn404WhenTaskNotFound() throws Exception {

        int id = 999;

        doThrow(new TaskNotFoundException(id))
                .when(adminTaskService)
                .restoreTask(eq(id), any());

        mockMvc.perform(post("/tasks/restore/{id}", id)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}