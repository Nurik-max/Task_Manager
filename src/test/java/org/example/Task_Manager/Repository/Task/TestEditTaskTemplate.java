package org.example.Task_Manager.Repository.Task;

import org.example.Task_Manager.Controller.TaskController;
import org.example.Task_Manager.DTO.TaskDTO;
import org.example.Task_Manager.Repoitory.TaskRepository;
import org.example.Task_Manager.Repoitory.WorkerRepository;
import org.example.Task_Manager.Sevice.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

// Импорты ТОЛЬКО для MockMvc (никаких MockRest!)
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.*;
@WebMvcTest(TaskController.class)
public class TestEditTaskTemplate {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private WorkerRepository workerRepository;
    @Test
    public void TestEditTask() throws Exception {
        TaskDTO testTask = new TaskDTO();
        testTask.setDescription("Test Task - success"); // Запомни эту строку
        testTask.setId(1);

//        when(taskService.showTask(1)).thenReturn(testTask);

        mockMvc.perform(get("/tasks/1/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("task", hasProperty("description", is("Test Task - success"))))
                .andExpect(model().attribute("task", hasProperty("id", is(1))));
    }
}