package org.example.Task_Manager.Controller;

import org.example.Task_Manager.DTO.TaskDTO;
import org.example.Task_Manager.Model.Status;
import org.example.Task_Manager.Repoitory.WorkerRepository;
import org.example.Task_Manager.Sevice.AdminTaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerEditTaskTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminTaskService adminTaskService;

    @MockBean
    private WorkerRepository workerRepository;

    @Test
    void editTask() throws Exception {

        int id = 1;
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(id);
        taskDTO.setStatus(Status.DONE);

//        when(taskService.showTask(id)).thenReturn(taskDTO);

        mockMvc.perform(get("/tasks/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/edit"))
                .andExpect(model().attributeExists("task"))
                .andExpect(model().attributeExists("workers"))
                .andExpect(model().attribute("task", hasProperty("id", is(1))));
    }

    @Test
    void shouldReturnTaskNotFound() throws Exception{

        int id = 999;

//        doThrow( new TaskNotFoundException(id))
//                .when(taskService)
//                        .showTask(id);

        mockMvc.perform(get("/tasks/999/edit", id)
                        .with(csrf()))
                .andExpect(status().isNotFound());

    }
}