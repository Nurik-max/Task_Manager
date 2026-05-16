package org.example.Task_Manager.Sevice.Task;

import org.example.Task_Manager.DTO.TaskDTO;
import org.example.Task_Manager.Repoitory.TaskRepository;
import org.example.Task_Manager.Repoitory.WorkerRepository;
import org.example.Task_Manager.Sevice.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.*;

@WebMvcTest(TestTaskServiceUpdateTask.class)
public class TestTaskServiceUpdateTask {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository taskRepository;
    @MockBean
    private TaskService taskService;
    @MockBean
    private WorkerRepository workerRepository;

    @Test
    public void testUpdateTask() throws Exception {
        TaskDTO mockResponse = new TaskDTO();
        mockResponse.setId(1);
        mockResponse.setDescription("chosen worker");
        mockResponse.setWorkerName("Gojo Satoru"); // Просто строка!

        when(taskService.updateTask(eq(1), (TaskDTO) any(TaskDTO.class))).thenReturn(mockResponse);

        mockMvc.perform(get("/tasks/1/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("task", hasProperty("description", is("chosen worker"))))
                // Теперь проверка очень простая:
                .andExpect(model().attribute("task", hasProperty("workerName", is("Gojo Satoru"))));
    }
}