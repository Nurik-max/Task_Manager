package org.example.Task_Manager.Controller.rest;

import org.example.Task_Manager.Controller.security.CustomAuthenticationSuccessHandler;
import org.example.Task_Manager.DTO.tasks.response.AdminResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
@WebMvcTest(controllers = TaskRestController.class)
@Import(SecurityConfig.class)
class TRC_getAllMyTasks {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomAuthenticationSuccessHandler successHandler;

    @MockBean
    private AdminTaskService adminTaskService;

    @MockBean
    private UserTaskService userTaskService;

    @Test
    void getAllMyTasks() throws Exception {

        Worker user = new Worker();
        user.setUserRole(UserRole.USER);
        user.setId(1);

        WorkerDetails userDetails = new WorkerDetails(user);

        Pageable pageable = PageRequest.of(0,10);

       UserResponse response = new UserResponse();
        response.setId(2);
        response.setDescription("Good luck today");
        response.setPriority(Priority.MEDIUM);


        Page<UserResponse> userResponses = new PageImpl<>(List.of(response), pageable, 1);

        when(userTaskService.workerListOfTask(userDetails, null, null, null, null, null, pageable, false))
                .thenReturn(userResponses);


        mockMvc.perform(get("/api/tasks/my")
                        .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(2))
                .andExpect(jsonPath("$.content[0].description").value("Good luck today"))
                .andExpect(jsonPath("$.content[0].priority").value(Priority.MEDIUM.toString()));

        verify(userTaskService).workerListOfTask(userDetails, null, null, null, null, null, pageable, false);
    }

    @Test
    void getAllMyTasksWithFilters() throws Exception{

        Worker user = new Worker();
        user.setUserRole(UserRole.USER);
        user.setId(1);

        WorkerDetails userDetails = new WorkerDetails(user);

        Pageable pageable = PageRequest.of(1,10);

        UserResponse response = new UserResponse();
        response.setId(2);

        Page<UserResponse> userResponses = new PageImpl<>(List.of(response), pageable, 1);

        when(userTaskService.workerListOfTask(userDetails, Status.NEW, Priority.HIGH, "test", null, null, pageable, true))
                .thenReturn(userResponses);

        mockMvc.perform(get("/api/tasks/my")
                .with(user(userDetails))
                        .param("page","1")
                .param("status", "NEW")
                .param("priority", "HIGH")
                .param("keyword", "test")
                .param("isDeleted", "true")
        ).andExpect(status().isOk());

        verify(userTaskService).workerListOfTask(
                userDetails, Status.NEW, Priority.HIGH, "test", null, null, pageable, true);

    }
}