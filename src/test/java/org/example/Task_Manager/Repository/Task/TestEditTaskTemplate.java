package org.example.Task_Manager.Repository.Task;

import org.example.Task_Manager.Controller.mvc.TaskController;
import org.example.Task_Manager.DTO.tasks.TaskDTO;
import org.example.Task_Manager.Repository.TaskRepository;
import org.example.Task_Manager.Repository.WorkerRepository;
import org.example.Task_Manager.Sevice.AdminTaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

// Импорты ТОЛЬКО для MockMvc (никаких MockRest!)
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.*;
@WebMvcTest(TaskController.class)
public class TestEditTaskTemplate {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminTaskService adminTaskService;

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