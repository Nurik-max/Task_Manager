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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskControllerHardDeleteTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminTaskService adminTaskService;

    @MockBean
    UserTaskService userTaskService;

    @MockBean
    private WorkerRepository workerRepository;

    @Test
    void hardDeleteTask() throws Exception {
        int id = 1;
        Worker mockWorker = new Worker();
        mockWorker.setUserRole(UserRole.USER);
        WorkerDetails mockWorkerDetails = new WorkerDetails(mockWorker);

        mockMvc.perform(post("/tasks/hard-delete/{id}", id)
                        .with(csrf()).with(user(mockWorkerDetails)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks/trash"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturn404WhenTaskNotFound() throws Exception{

        int id = 999;

        doThrow(new TaskNotFoundException(id))
                .when(adminTaskService)
                .hardDeleteTask(eq(id), any());

        mockMvc.perform(post("/tasks/hard-delete/{id}", id)
                .with(csrf()))
                .andExpect(status().isNotFound());
    }
}