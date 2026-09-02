package org.example.Task_Manager.Sevice.Task;


import org.example.Task_Manager.Controller.mvc.TaskController;
import org.example.Task_Manager.DTO.tasks.request.AdminUpdateTaskRequest;
import org.example.Task_Manager.DTO.tasks.response.AdminResponse;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestTaskServiceUpdateTask {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository taskRepository;
    @MockBean
    private AdminTaskService adminTaskService;
    @MockBean
    private UserTaskService  userTaskService;
    @MockBean
    private WorkerRepository workerRepository;

    @Test
    public void testUpdateTask() throws Exception {
        int id = 1;
        AdminResponse mockResponse = new AdminResponse();
        mockResponse.setId(id);
        mockResponse.setDescription("chosen worker");
        mockResponse.setStatus(Status.DONE);


        Worker worker = new Worker();
        worker.setUserRole(UserRole.ADMIN);
        WorkerDetails workerDetails = new WorkerDetails(worker);
      // Просто строка!

//        when(adminTaskService.updateTask(eq(1), eq(mockResponse), eq(workerDetails))).thenReturn(new AdminResponse());
        when(adminTaskService.showTask(
                eq(1),
                any(WorkerDetails.class) // Просто проверяем тип
        )).thenReturn(mockResponse);


        mockMvc.perform(get("/tasks/{id}/edit", id)
                        .with(user(workerDetails))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("task", hasProperty("description", is("chosen worker"))))
                // Теперь проверка очень простая:
                .andExpect(model().attribute("task", hasProperty("status", is(Status.DONE))))
        .andExpect(model().attribute("task", hasProperty("id", is(id))))
                .andExpect(view().name("tasks/edit"));
    }
}