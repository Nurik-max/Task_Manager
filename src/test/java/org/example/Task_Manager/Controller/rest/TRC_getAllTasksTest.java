package org.example.Task_Manager.Controller.rest;

import org.example.Task_Manager.Controller.security.CustomAuthenticationSuccessHandler;
import org.example.Task_Manager.DTO.tasks.response.AdminResponse;
import org.example.Task_Manager.Model.*;
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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TaskRestController.class)
@Import(SecurityConfig.class)
class TRC_getAllTasksTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomAuthenticationSuccessHandler successHandler;

    @MockBean
    private AdminTaskService adminTaskService;

    @MockBean
    private UserTaskService userTaskService;

    @Test
    void getAllTasks() throws Exception {

        Worker admin = new Worker();
        admin.setUserRole(UserRole.ADMIN);
        admin.setId(1);

        WorkerDetails adminDetails = new WorkerDetails(admin);

        Pageable pageable = PageRequest.of(0,10);

        AdminResponse response = new AdminResponse();
        response.setId(2);
        response.setDescription("Good luck today");
        response.setUsername("Nurik");

        Page<AdminResponse> adminResponse = new PageImpl<>(List.of(response), pageable, 1);

        when(adminTaskService.getTasks(null, null, null, null, null, null, false, pageable))
                .thenReturn(adminResponse);


        mockMvc.perform(get("/api/tasks")
                .with(user(adminDetails)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(2))
        .andExpect(jsonPath("$.content[0].description").value("Good luck today"))
        .andExpect(jsonPath("$.content[0].username").value("Nurik"));

        verify(adminTaskService).getTasks(null, null, null, null, null, null, false, pageable);

    }

    @Test
    void getAllTasksWithFilters() throws Exception {

        Worker admin = new Worker();
        admin.setUserRole(UserRole.ADMIN);
        admin.setId(1);

        WorkerDetails adminDetails = new WorkerDetails(admin);

        Pageable pageable = PageRequest.of(2, 10);

        Page<AdminResponse> page =
                new PageImpl<>(List.of(), pageable, 0);

        when(adminTaskService.getTasks(
                Status.IN_PROGRESS,
                Priority.HIGH,
                "test",
                "nurik",
                null,
                null,
                false,
                pageable
        )).thenReturn(page);

        mockMvc.perform(get("/api/tasks")
                        .with(user(adminDetails))
                        .param("page", "2")
                        .param("status", "IN_PROGRESS")
                        .param("priority", "HIGH")
                        .param("keyword", "test")
                        .param("username", "nurik")
                        .param("isDeleted", "false"))
                .andExpect(status().isOk());

        verify(adminTaskService).getTasks(
                Status.IN_PROGRESS,
                Priority.HIGH,
                "test",
                "nurik",
                null,
                null,
                false,
                pageable
        );
    }

    @Test
    void getAllTasks_forUser_forbidden() throws Exception {

        Worker user = new Worker();
        user.setUserRole(UserRole.USER);
        user.setId(2);

        WorkerDetails userDetails = new WorkerDetails(user);

        mockMvc.perform(get("/api/tasks")
                        .with(user(userDetails)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(adminTaskService);
    }
}