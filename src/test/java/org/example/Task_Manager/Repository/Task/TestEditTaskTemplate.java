package org.example.Task_Manager.Repository.Task;

import org.example.Task_Manager.Controller.mvc.TaskController;
import org.example.Task_Manager.DTO.tasks.TaskDTO;
import org.example.Task_Manager.DTO.tasks.response.AdminResponse;
import org.example.Task_Manager.Model.UserRole;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Repository.TaskRepository;
import org.example.Task_Manager.Repository.WorkerRepository;
import org.example.Task_Manager.Sevice.AdminTaskService;
import org.example.Task_Manager.Sevice.UserTaskService;
import org.example.Task_Manager.details.WorkerDetails;
import org.junit.jupiter.api.Test;
import org.mockito.internal.stubbing.BaseStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

// Импорты ТОЛЬКО для MockMvc (никаких MockRest!)
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
@WebMvcTest(TaskController.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@AutoConfigureMockMvc(addFilters = false)
public class TestEditTaskTemplate {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminTaskService adminTaskService;

    @MockBean
    private UserTaskService userTaskService;

   @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private WorkerRepository workerRepository;

    @Test
    public void TestEditTask() throws Exception {
        Worker mockWorker = new Worker();
        mockWorker.setUserRole(UserRole.USER);
        WorkerDetails mockWorkerDetails = new WorkerDetails(mockWorker);

        AdminResponse testTask = new AdminResponse();
        testTask.setDescription("Test Task - success"); // Запомни эту строку
        testTask.setId(1);

        when(adminTaskService.showTask(eq(1), any())).thenReturn(testTask);

        mockMvc.perform(get("/tasks/1/edit")
                .with(user(mockWorkerDetails)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("task", hasProperty("description", is("Test Task - success"))))
                .andExpect(model().attribute("task", hasProperty("id", is(1))));
    }
}