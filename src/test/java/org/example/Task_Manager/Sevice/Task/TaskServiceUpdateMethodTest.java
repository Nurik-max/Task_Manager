package org.example.Task_Manager.Sevice.Task;

import org.example.Task_Manager.DTO.TaskDTO;
import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Repoitory.TaskMapper;
import org.example.Task_Manager.Repoitory.TaskRepository;
import org.example.Task_Manager.Sevice.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceUpdateMethodTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;
//
////    @Test
//    void shouldUpdateTask() {
//        int taskId = 1;
//
//        TaskDTO dto = new TaskDTO();
//        dto.setDescription("chosen worker");
//        dto.setWorkerName("Gojo Satoru");
//
//        Task existingTask = new Task();
//        existingTask.setId(taskId);
//
//        Task updatedTask = new Task();
//        updatedTask.setId(taskId);
//
//        // mock
//        when(taskRepository.findById(taskId))
//                .thenReturn(Optional.of(existingTask));
//
//        when(taskMapper.toEntity(dto))
//                .thenReturn(updatedTask);
//
//        when(taskRepository.save(updatedTask))
//                .thenReturn(updatedTask);
//
//        when(taskMapper.toDTO(updatedTask))
//                .thenReturn(dto);
//
//        // act
//        TaskDTO result = taskService.updateTask(taskId, dto);
//
//        // assert
//        assertNotNull(result);
//        assertEquals("chosen worker", result.getDescription());
//
//        // verify
//        verify(taskRepository).findById(taskId);
//        verify(taskRepository).save(updatedTask);
//    }
}