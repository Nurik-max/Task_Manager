package org.example.Task_Manager.Controller.MVC;

import org.example.Task_Manager.Controller.security.CustomAuthenticationSuccessHandler;
import org.example.Task_Manager.DTO.tasks.request.AdminUpdateTaskRequest;
import org.example.Task_Manager.Model.UserRole;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Repository.WorkerRepository;
import org.example.Task_Manager.Sevice.AdminTaskService;
import org.example.Task_Manager.Sevice.UserTaskService;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@ActiveProfiles("test")
@Import(SecurityConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskControllerUpdateTaskTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminTaskService adminTaskService;

    @MockBean
    private UserTaskService userTaskService;

    @MockBean
    private WorkerRepository workerRepository;

    @MockBean
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Test
    void updateTask() throws Exception {

        int id = 1;
        Worker mockWorker = new Worker();
        mockWorker.setUserRole(UserRole.ADMIN);
        mockWorker.setId(id);
        WorkerDetails mockWorkerDetails = new WorkerDetails(mockWorker);

        mockMvc.perform(post("/tasks/{id}/admin", id)
                        .with(user(mockWorkerDetails))
                        .with(csrf())
                        .param("description", "Updated task")
                        .param("status", "IN_PROGRESS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));

        verify(adminTaskService).updateTask(eq(id), any(AdminUpdateTaskRequest.class), eq(mockWorkerDetails));
    }

    @Test
    void updateTaskShouldReturnForbiddenForUser() throws Exception {

        int id = 1;

        Worker mockWorker = new Worker();
        mockWorker.setUserRole(UserRole.USER);

        WorkerDetails mockWorkerDetails = new WorkerDetails(mockWorker);

        mockMvc.perform(post("/tasks/{id}/admin", id)
                        .with(user(mockWorkerDetails))
                        .with(csrf())
                        .param("description", "Updated task"))
                .andExpect(status().isForbidden());
    }
}