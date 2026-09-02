package org.example.Task_Manager.Controller;

import org.example.Task_Manager.Controller.mvc.TaskController;
import org.example.Task_Manager.DTO.tasks.TaskDTO;
import org.example.Task_Manager.DTO.tasks.response.AdminResponse;
import org.example.Task_Manager.Exceptions.TaskNotFoundException;
import org.example.Task_Manager.Model.Status;
import org.example.Task_Manager.Model.UserRole;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Repository.TaskRepository;
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
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(TaskController.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskControllerShowTaskTest {

    @MockBean
    private AdminTaskService adminTaskService;

    @MockBean
    private UserTaskService userTaskService;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private WorkerRepository workerRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void showTask() throws Exception {

        int id = 1;
        AdminResponse response = new AdminResponse();
        response.setId(id);
        response.setStatus(Status.IN_PROGRESS);

        Worker mockWorker = new Worker();
        mockWorker.setUserRole(UserRole.USER);
        WorkerDetails mockWorkerDetails = new WorkerDetails(mockWorker);


        when(adminTaskService.showTask(eq(id), any())).thenReturn(response);

        mockMvc.perform(get("/tasks/1")
                        .with(user(mockWorkerDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/view"))
                .andExpect(model().attributeExists("task"))
                .andExpect(model().attribute("task", response));
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenWorkerNotFound() throws Exception {

        int id = 999;

        doThrow(new TaskNotFoundException(id))
                .when(adminTaskService)
                .showTask(eq(id), any());

        mockMvc.perform(post("/tasks/view/{id}", id)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}