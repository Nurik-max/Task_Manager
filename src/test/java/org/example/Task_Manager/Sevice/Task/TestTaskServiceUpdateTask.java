package org.example.Task_Manager.Sevice.Task;

import org.example.Task_Manager.Repository.TaskRepository;
import org.example.Task_Manager.Repository.WorkerRepository;
import org.example.Task_Manager.Sevice.AdminTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(TestTaskServiceUpdateTask.class)
public class TestTaskServiceUpdateTask {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository taskRepository;
    @MockBean
    private AdminTaskService adminTaskService;
    @MockBean
    private WorkerRepository workerRepository;

//    @Test
//    public void testUpdateTask() throws Exception {
//        TaskDTO mockResponse = new TaskDTO();
//        mockResponse.setId(1);
//        mockResponse.setDescription("chosen worker");
//        mockResponse.setWorkerName("Gojo Satoru"); // Просто строка!
//
//        when(taskService.updateTask(eq(1), (TaskDTO) any(TaskDTO.class))).thenReturn(mockResponse);
//
//        mockMvc.perform(get("/tasks/1/edit"))
//                .andExpect(status().isOk())
//                .andExpect(model().attribute("task", hasProperty("description", is("chosen worker"))))
//                // Теперь проверка очень простая:
//                .andExpect(model().attribute("task", hasProperty("workerName", is("Gojo Satoru"))));
//    }
}