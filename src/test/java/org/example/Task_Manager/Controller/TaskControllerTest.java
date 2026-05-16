package org.example.Task_Manager.Controller;
import org.example.Task_Manager.DTO.TaskDTO;
import org.example.Task_Manager.Repoitory.TaskRepository;
import org.example.Task_Manager.Repoitory.WorkerRepository;
import org.example.Task_Manager.Sevice.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.ArgumentMatchers.any; // 🔥 Важный правильный импорт
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private TaskService taskService;

    @MockBean
    private WorkerRepository workerRepository;

    @Test
    public void testNewTaskForm() throws Exception{


        mockMvc.perform(get("/tasks/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/new")) // Проверяем имя view
                .andExpect(model().attributeExists("task")) // Проверяем, что модель содержит "task"
                .andExpect(model().attribute("task", hasProperty("description", nullValue()))); // Проверяем, что поле пустое
    }

    @Test
    public void testSaveTask() throws Exception {
        // Настройка мока: когда saveTask вызывается с любым объектом TaskDTO, возвращаем что-то
//        when(taskService.saveTask(any(TaskDTO.class))).thenReturn(new TaskDTO());

        mockMvc.perform(post("/tasks")
                        .param("description", "Make test for TaskService")
                        .param("workerName", "Nurik"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));

        // Проверка, что метод был вызван
//        verify(taskService).saveTask(any(TaskDTO.class));
    }
}
