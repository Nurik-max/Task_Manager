package org.example.Task_Manager.Controller.rest;

import org.example.Task_Manager.Controller.security.CustomAuthenticationSuccessHandler;
import org.example.Task_Manager.DTO.tasks.request.CreateTaskRequest;
import org.example.Task_Manager.DTO.tasks.request.UpdateTaskRequest;
import org.example.Task_Manager.DTO.tasks.response.UserResponse;
import org.example.Task_Manager.Model.Priority;
import org.example.Task_Manager.Model.Status;
import org.example.Task_Manager.Model.UserRole;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Sevice.AdminTaskService;
import org.example.Task_Manager.Sevice.UserTaskService;
import org.example.Task_Manager.config.SecurityConfig;
import org.example.Task_Manager.details.WorkerDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
@WebMvcTest(TaskRestController.class)
@Import(SecurityConfig.class)
class TRC_createMyTaskTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomAuthenticationSuccessHandler successHandler;

    @MockBean
    private AdminTaskService adminTaskService;

    @MockBean
    private UserTaskService userTaskService;

    @Test
    void createMyTasks() throws Exception {

        Worker user = new Worker();
        user.setUserRole(UserRole.USER);

        WorkerDetails workerDetails = new WorkerDetails(user);

        CreateTaskRequest request = new CreateTaskRequest();
        request.setDescription("My Task");
        request.setPriority(Priority.HIGH);

        UserResponse  userResponse = new UserResponse();
        userResponse.setDescription("My Task");
        userResponse.setPriority(Priority.HIGH);

        when(userTaskService.saveUserTask(any(CreateTaskRequest.class), eq(workerDetails)) ).thenReturn(userResponse);


        mockMvc.perform(post("/api/tasks/user")
                        .with(user(workerDetails))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "description": "My Task",
                        "priority": "HIGH"
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description").value("My Task"))
                .andExpect(jsonPath("$.priority").value("HIGH"));

        verify(userTaskService).saveUserTask(
                argThat(dto->
                        dto.getDescription().equals("My Task")
                        && dto.getPriority().equals(Priority.HIGH)),
                eq(workerDetails)
        );
    }

    @Test
    void shouldReturnUnauthorized() throws Exception {

        mockMvc.perform(patch("/api/tasks/user")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))

                .andExpect(status().isUnauthorized());

        verifyNoInteractions(userTaskService);

    }
}