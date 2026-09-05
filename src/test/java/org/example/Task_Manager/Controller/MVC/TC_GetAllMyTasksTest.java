package org.example.Task_Manager.Controller.MVC;
import org.example.Task_Manager.DTO.tasks.response.UserResponse;
import org.example.Task_Manager.Model.Priority;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.ArgumentMatchers.any; // 🔥 Важный правильный импорт
import static org.mockito.Mockito.verify;

@WebMvcTest(TaskController.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TC_GetAllMyTasksTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private AdminTaskService adminTaskService;

    @MockBean
    private UserTaskService userTaskService;

    @MockBean
    private WorkerRepository workerRepository;



    @Test
    void getAllMyTasks() throws Exception{
        int id = 1;
        Worker worker = new Worker();
        worker.setId(id);
        worker.setUserRole(UserRole.USER);
        WorkerDetails workerDetails = new WorkerDetails(worker);

        Pageable pageable = PageRequest.of(0, 10);

        Page<UserResponse> responses = new PageImpl<>(new ArrayList<>());


        when(userTaskService.workerListOfTask(workerDetails, null, null, null, null, null, pageable, false))
                .thenReturn(responses);

        mockMvc.perform(get("/tasks/my")
                .with(user(workerDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/tasks"))
                .andExpect(model().attributeExists("tasks"));

        verify(userTaskService).workerListOfTask(workerDetails, null, null, null, null, null, pageable, false);


    }

    @Test
    void getAllMyTasksWithParam() throws Exception{
        int id = 1;
        Worker worker = new Worker();
        worker.setId(id);
        worker.setUserRole(UserRole.USER);
        WorkerDetails workerDetails = new WorkerDetails(worker);

        Status status = Status.NEW;
        Priority priority = Priority.MEDIUM;

        Pageable pageable = PageRequest.of(0, 10);
UserResponse userResponse = new UserResponse();
userResponse.setDescription("description");
userResponse.setStatus(status);
userResponse.setPriority(priority);

        Page<UserResponse> responses = new PageImpl<>(List.of(userResponse));

        when(userTaskService.workerListOfTask(workerDetails, status, priority, null, null, null, pageable, false))
                .thenReturn(responses);

        mockMvc.perform(get("/tasks/my")
                        .param("status", status.toString())
                        .param("priority", priority.toString())
                        .with(user(workerDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/tasks"))
                .andExpect(model().attributeExists("tasks"));
        verify(userTaskService).workerListOfTask(workerDetails, status, priority, null, null, null, pageable, false);


    }

    @Test
    void getAllMyTasksShouldHandleInvalidFilters() throws Exception {

        int id = 1;
        Worker worker = new Worker();
        worker.setId(id);
        worker.setUserRole(UserRole.USER);
        WorkerDetails workerDetails = new WorkerDetails(worker);


        Pageable pageable = PageRequest.of(0, 10);

        Page<UserResponse> responses = new PageImpl<>(new ArrayList<>());

        when(userTaskService.workerListOfTask(workerDetails, null, null, null, null, null, pageable, false))
                .thenReturn(responses);

        mockMvc.perform(get("/tasks/my")
                        .param("status", "INVALID")
                        .param("priority", "ILLEGAL")
                        .with(user(workerDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/tasks"))
                .andExpect(model().attributeExists("tasks"));
        verify(userTaskService).workerListOfTask(workerDetails, null, null, null, null, null, pageable, false);
    }
}
