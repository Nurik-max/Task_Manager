package org.example.Task_Manager.Controller.MVC;

import org.example.Task_Manager.Controller.security.CustomAuthenticationSuccessHandler;
import org.example.Task_Manager.DTO.workers.response.WorkerDTO;
import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Model.UserRole;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Repository.WorkerMapper;
import org.example.Task_Manager.Repository.WorkerRepository;
import org.example.Task_Manager.Sevice.AdminTaskService;
import org.example.Task_Manager.Sevice.WorkerService;
import org.example.Task_Manager.config.SecurityConfig;
import org.example.Task_Manager.details.WorkerDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = WorkerController.class)
@Import(SecurityConfig.class)
class WC_ShowOrEditWorkerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomAuthenticationSuccessHandler successHandler;

    @MockBean
    private WorkerService workerService;

    @MockBean
    private AdminTaskService adminTaskService;

    @MockBean
    private WorkerMapper workerMapper;

    @MockBean
    private WorkerRepository workerRepository;


    @Test
    void showOrEditWorker() throws Exception {

        Worker admin = new Worker();
        admin.setId(1);
        admin.setUserRole(UserRole.ADMIN);

        WorkerDetails workerDetails = new WorkerDetails(admin);

        WorkerDTO user = new WorkerDTO();
        user.setId(2);
        user.setUserRole(UserRole.USER);

        Worker another = new Worker();
        another.setId(3);

        List<Task> tasks = new ArrayList<>();

        List<Worker> allWorkers = new ArrayList<>();
        allWorkers.add(admin);
        allWorkers.add(another);

        when(workerService.showWorker(user.getId())).thenReturn(user);
        when(adminTaskService.getTasksByWorkerId(user.getId())).thenReturn(tasks);
        when(workerService.findAllExcept(user.getId())).thenReturn(allWorkers);

        mockMvc.perform(get("/workers/{id}/edit",  user.getId())
                .with(user(workerDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("workers/edit"))
                .andExpect(model().attribute("worker", user))
                .andExpect(model().attribute("tasks", tasks))
                .andExpect(model().attribute("allWorkers", allWorkers));

        verify(workerService).showWorker(user.getId());
        verify(adminTaskService).getTasksByWorkerId(user.getId());
        verify(workerService).findAllExcept(user.getId());


    }

    @Test
    void shouldShowEditPageWhenUserEditsHimself() throws Exception {
        int workerId = 5;
        Worker user = new Worker();
        user.setId(workerId);
        user.setUserRole(UserRole.USER);

        WorkerDetails workerDetails = new WorkerDetails(user);

        WorkerDTO workerDTO = new WorkerDTO();

        when(workerService.showWorker(5))
                .thenReturn(workerDTO);

        when(adminTaskService.getTasksByWorkerId(5))
                .thenReturn(List.of());

        when(workerService.findAllExcept(5))
                .thenReturn(List.of());

        mockMvc.perform(get("/workers/{id}/edit", 5)
                        .with(user(workerDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("workers/edit"))
                .andExpect(model().attribute("worker", workerDTO))
                .andExpect(model().attribute("tasks", List.of()))
                .andExpect(model().attribute("allWorkers", List.of()));

    }

    @Test
    void shouldReturnForbiddenWhenUserEditsAnotherWorker() throws Exception {

        int authenticatedUserId = 5;
        int requestedWorkerId = 10;

        Worker user = new Worker();
        user.setId(authenticatedUserId);
        user.setUserRole(UserRole.USER);

        WorkerDetails workerDetails = new WorkerDetails(user);

        mockMvc.perform(get("/workers/{id}/edit", requestedWorkerId)
                        .with(user(workerDetails)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(workerService, adminTaskService);
    }
}