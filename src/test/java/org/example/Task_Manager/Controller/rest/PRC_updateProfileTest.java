package org.example.Task_Manager.Controller.rest;

import org.example.Task_Manager.Controller.security.CustomAuthenticationSuccessHandler;
import org.example.Task_Manager.DTO.workers.request.ProfileUpdateDTO;
import org.example.Task_Manager.DTO.workers.response.WorkerDTO;
import org.example.Task_Manager.Model.Priority;
import org.example.Task_Manager.Model.UserRole;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Sevice.TaskStatisticsService;
import org.example.Task_Manager.Sevice.WorkerService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfileRestController.class)
@Import(SecurityConfig.class)
class PRC_updateProfileTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkerService workerService;

    @MockBean
    private TaskStatisticsService taskStatisticsService;

    @MockBean
    private CustomAuthenticationSuccessHandler successHandler;

    @Test
    void updateWorkerProfileForAdmin() throws Exception {

        Worker admin = new Worker();
        admin.setUserRole(UserRole.ADMIN);
        admin.setId(1);

        WorkerDetails workerDetails = new WorkerDetails(admin);

        ProfileUpdateDTO profileUpdateDTO = new ProfileUpdateDTO();
        profileUpdateDTO.setUsername("username");
        profileUpdateDTO.setEmail("nurik@example.com");
        profileUpdateDTO.setSurname("surname");
        profileUpdateDTO.setPosition("position");
        profileUpdateDTO.setPhone("phone");

        WorkerDTO updateWorker = new WorkerDTO();
        updateWorker.setUsername("username");
        updateWorker.setEmail("nurik@example.com");
        updateWorker.setSurname("surname");
        updateWorker.setPosition("position");
        updateWorker.setPhone("phone");

        when(workerService.updateProfile(anyInt(), any(ProfileUpdateDTO.class))).thenReturn(updateWorker);

        mockMvc.perform(put("/api/profile/update")
                .with(user(workerDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "username",
                        "email": "nurik@example.com",
                        "surname": "surname",
                        "position": "position",
                        "phone": "phone"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("username"))
                .andExpect(jsonPath("$.surname").value("surname"))
                .andExpect(jsonPath("$.email").value("nurik@example.com"))
                .andExpect(jsonPath("$.position").value("position"))
                .andExpect(jsonPath("$.phone").value("phone"));

        verify(workerService).updateProfile(eq(workerDetails.getWorker().getId()),
                argThat(dto -> dto.getUsername().equals("username") && dto.getEmail().equals("nurik@example.com")
                && dto.getPhone().equals("phone") && dto.getSurname().equals("surname") && dto.getPosition().equals("position"))
        );
    }

    @Test
    void updateWorkerProfileForUser() throws Exception {

        Worker user = new Worker();
        user.setUserRole(UserRole.USER);
        user.setId(1);

        WorkerDetails workerDetails = new WorkerDetails(user);

        ProfileUpdateDTO profileUpdateDTO = new ProfileUpdateDTO();
        profileUpdateDTO.setUsername("username");
        profileUpdateDTO.setEmail("nurik@example.com");
        profileUpdateDTO.setSurname("surname");
        profileUpdateDTO.setPosition("position");
        profileUpdateDTO.setPhone("phone");

        WorkerDTO updateWorker = new WorkerDTO();
        updateWorker.setUsername("username");
        updateWorker.setEmail("nurik@example.com");
        updateWorker.setSurname("surname");
        updateWorker.setPosition("position");
        updateWorker.setPhone("phone");

        when(workerService.updateProfile(anyInt(), any(ProfileUpdateDTO.class))).thenReturn(updateWorker);

        mockMvc.perform(put("/api/profile/update")
                        .with(user(workerDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                        "username": "username",
                        "email": "nurik@example.com",
                        "surname": "surname",
                        "position": "position",
                        "phone": "phone"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("username"))
                .andExpect(jsonPath("$.surname").value("surname"))
                .andExpect(jsonPath("$.email").value("nurik@example.com"))
                .andExpect(jsonPath("$.position").value("position"))
                .andExpect(jsonPath("$.phone").value("phone"));

        verify(workerService).updateProfile(eq(workerDetails.getWorker().getId()),
                argThat(dto -> dto.getUsername().equals("username") && dto.getEmail().equals("nurik@example.com")
                        && dto.getPhone().equals("phone") && dto.getSurname().equals("surname") && dto.getPosition().equals("position"))
        );
    }

    @Test
    void shouldReturnUnauthorized() throws Exception {

        mockMvc.perform(put("/api/profile/update")
                        .with(csrf()))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(workerService, taskStatisticsService);

    }
}