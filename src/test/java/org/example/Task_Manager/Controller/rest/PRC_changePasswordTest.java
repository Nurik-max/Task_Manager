package org.example.Task_Manager.Controller.rest;

import org.example.Task_Manager.Controller.security.CustomAuthenticationSuccessHandler;
import org.example.Task_Manager.DTO.workers.ChangePasswordDTO;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfileRestController.class)
@Import(SecurityConfig.class)
class PRC_changePasswordTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkerService workerService;

    @MockBean
    private TaskStatisticsService taskStatisticsService;

    @MockBean
    private CustomAuthenticationSuccessHandler successHandler;

    @Test
    void changePasswordForAdmin() throws Exception {

        Worker admin = new Worker();
        admin.setUserRole(UserRole.ADMIN);
        admin.setId(1);

        WorkerDetails workerDetails = new WorkerDetails(admin);

        ChangePasswordDTO  changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setNewPassword("1234");
        changePasswordDTO.setOldPassword("5678");
        changePasswordDTO.setConfirmPassword("confirmPassword");

        mockMvc.perform(post("/api/profile/password")
                .with(user(workerDetails))
                .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "newPassword": "1234",
                        "oldPassword": "5678",
                        "confirmPassword": "confirmPassword"                        
                        }
                        """))
                .andExpect(status().isOk())
                        .andExpect(content().string(""));

        verify(workerService).changePassword(eq(workerDetails.getWorker().getId()),
                argThat(dto -> dto.getNewPassword().equals(changePasswordDTO.getNewPassword())
                && dto.getOldPassword().equals(changePasswordDTO.getOldPassword())
                && dto.getConfirmPassword().equals(changePasswordDTO.getConfirmPassword()))
        );
    }

    @Test
    void changePasswordForUser() throws Exception {

        Worker user = new Worker();
        user.setUserRole(UserRole.USER);
        user.setId(1);

        WorkerDetails workerDetails = new WorkerDetails(user);

        ChangePasswordDTO  changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setNewPassword("1234");
        changePasswordDTO.setOldPassword("5678");
        changePasswordDTO.setConfirmPassword("confirmPassword");

        mockMvc.perform(post("/api/profile/password")
                        .with(user(workerDetails))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                        "newPassword": "1234",
                        "oldPassword": "5678",
                        "confirmPassword": "confirmPassword"                        
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(workerService).changePassword(eq(workerDetails.getWorker().getId()),
                argThat(dto -> dto.getNewPassword().equals(changePasswordDTO.getNewPassword())
                        && dto.getOldPassword().equals(changePasswordDTO.getOldPassword())
                        && dto.getConfirmPassword().equals(changePasswordDTO.getConfirmPassword()))
        );
    }

    @Test
    void shouldReturnUnauthorized() throws Exception {

        mockMvc.perform(post("/api/profile/password")
                        .with(csrf()))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(workerService, taskStatisticsService);

    }
}