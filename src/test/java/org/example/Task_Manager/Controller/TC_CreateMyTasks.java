package org.example.Task_Manager.Controller;

import org.example.Task_Manager.Controller.mvc.TaskController;
import org.example.Task_Manager.DTO.tasks.request.CreateTaskRequest;
import org.example.Task_Manager.DTO.tasks.response.UserResponse;
import org.example.Task_Manager.Model.UserRole;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Repository.WorkerRepository;
import org.example.Task_Manager.Sevice.AdminTaskService;
import org.example.Task_Manager.Sevice.UserTaskService;
import org.example.Task_Manager.details.WorkerDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = TaskController.class)
class TC_CreateMyTasks {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private AdminTaskService adminTaskService;

    @MockBean
    UserTaskService userTaskService;

    @MockBean
    WorkerRepository workerRepository;


    @Test
    void createMyTasks() throws Exception {

        CreateTaskRequest request = new CreateTaskRequest();

        Worker worker = new Worker();
        worker.setId(1);
        worker.setUserRole(UserRole.USER);

        WorkerDetails workerDetails = new WorkerDetails(worker);

        mockMvc.perform(post("/tasks/create/user")
                        .with(user(workerDetails))
                        .with(csrf())
                        .flashAttr("task", request))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks/my"));

        verify(userTaskService)
                .saveUserTask(eq(request), eq(workerDetails));
    }
}