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
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class TaskServiceGetTasksTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;
    @Test
    void shouldReturnTasksPage() {

        // Arrange
        Pageable pageable = PageRequest.of(0, 5);

        Task task = new Task();
        TaskDTO dto = new TaskDTO();

        Page<Task> taskPage = new PageImpl<>(List.of(task));

        when(taskRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(taskPage);

        when(taskMapper.toDTO(task))
                .thenReturn(dto);

        // Act
        Page<TaskDTO> result = taskService.getTasks(
                null, null, null, null, null, false, pageable
        );

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());

        verify(taskRepository).findAll( any(Specification.class), eq(pageable));
        verify(taskMapper).toDTO(task);
    }
}