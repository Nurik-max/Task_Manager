package org.example.Task_Manager.Sevice.Task;

import org.example.Task_Manager.Repository.TaskMapper;
import org.example.Task_Manager.Repository.TaskRepository;
import org.example.Task_Manager.Sevice.AdminTaskService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AdminTaskServiceUpdateMethodTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private AdminTaskService adminTaskService;
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