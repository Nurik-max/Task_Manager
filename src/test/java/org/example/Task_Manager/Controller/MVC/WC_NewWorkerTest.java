package org.example.Task_Manager.Controller.MVC;

import org.example.Task_Manager.Controller.security.CustomAuthenticationSuccessHandler;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(controllers = WorkerController.class)
@Import(SecurityConfig.class)
class WC_NewWorkerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomAuthenticationSuccessHandler successHandler;

    @MockBean
    private WorkerService workerService;

    @MockBean
    private WorkerRepository workerRepository;

    @MockBean
    private WorkerMapper workerMapper;

    @MockBean
    private AdminTaskService adminTaskService;


    @Test
    void newWorker() throws Exception {

        Worker worker = new Worker();
        worker.setId(1);
        worker.setUserRole(UserRole.ADMIN);
        WorkerDetails workerDetails = new WorkerDetails(worker);

        mockMvc.perform(get("/workers/new")
                        .with(user(workerDetails)))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("worker"))
                .andExpect(view().name("workers/new"));


    }

    @Test
    void  shouldDenyAccessForUser() throws Exception {
        Worker worker = new Worker();
        worker.setId(1);
        worker.setUserRole(UserRole.USER);
        WorkerDetails workerDetails = new WorkerDetails(worker);

        mockMvc.perform(get("/workers/new")
                        .with(user(workerDetails)))
                .andExpect(status().isForbidden());

    }
}