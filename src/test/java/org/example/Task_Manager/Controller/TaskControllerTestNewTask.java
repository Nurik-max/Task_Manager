package org.example.Task_Manager.Controller;

import org.example.Task_Manager.Controller.mvc.TaskController;
import org.example.Task_Manager.Model.UserRole;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Repository.WorkerRepository;
import org.example.Task_Manager.Sevice.AdminTaskService;
import org.example.Task_Manager.Sevice.UserTaskService;
import org.example.Task_Manager.details.WorkerDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@WebMvcTest(TaskController.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskControllerTestNewTask {

    @Autowired
   private MockMvc mockMvc;

    @MockBean
   private AdminTaskService adminTaskService;

    @MockBean
    private UserTaskService userTaskService;

    @MockBean
   private WorkerRepository workerRepository;

@Test
@WithMockUser
  void newTask() throws Exception {

      Worker worker = new Worker();
      worker.setUserRole(UserRole.ADMIN);

      WorkerDetails workerDetails = new WorkerDetails(worker);

      mockMvc.perform(get("/tasks/new").with(user(workerDetails)))
              .andExpect(status().isOk())
              .andExpect(view().name("tasks/new"))
              .andExpect(model().attributeExists("task"))
              .andExpect(model().attributeExists("workers"));
  }


    @Test
    @WithMockUser
    void newTask1() throws Exception {

        Worker worker = new Worker();
        worker.setUserRole(UserRole.USER);

        WorkerDetails workerDetails = new WorkerDetails(worker);

        mockMvc.perform(get("/tasks/new").with(user(workerDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/new"))
                .andExpect(model().attributeExists("task"));
    }

}