package org.example.Task_Manager.Controller;

import org.example.Task_Manager.DTO.TaskDTO;
import org.example.Task_Manager.Model.Status;
import org.example.Task_Manager.Repoitory.TaskRepository;
import org.example.Task_Manager.Repoitory.WorkerRepository;
import org.example.Task_Manager.Sevice.AdminTaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(TaskController.class)
class TaskControllerShowTaskTest {

    @MockBean
    private AdminTaskService adminTaskService;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private WorkerRepository workerRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void showTask() throws Exception {

        int id = 1;
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(id);
        taskDTO.setStatus(Status.IN_PROGRESS);


//        when(taskService.showTask(id)).thenReturn(taskDTO);

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/view"))
                .andExpect(model().attributeExists("task"))
                .andExpect(model().attribute("task", taskDTO));
    }
}