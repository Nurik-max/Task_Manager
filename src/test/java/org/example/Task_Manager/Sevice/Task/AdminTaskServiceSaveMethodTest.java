package org.example.Task_Manager.Sevice.Task;
import org.example.Task_Manager.DTO.TaskDTO;
import org.example.Task_Manager.DTO.tasks.CreateTaskRequest;
import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Repoitory.TaskMapper;
import org.example.Task_Manager.Repoitory.TaskRepository;
import org.example.Task_Manager.Sevice.AdminTaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class AdminTaskServiceSaveMethodTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private AdminTaskService adminTaskService;

    // 1. Добавляем Mock для маппера, чтобы он не был null
    @Mock
    private TaskMapper taskMapper;

    @Test
    void shouldSaveTask() {

        TaskDTO dto = new TaskDTO();
        dto.setDescription("hello");

        Task taskEntity = new Task();

        // Настройка поведения репозитория
        when(taskMapper.toEntity(any(CreateTaskRequest.class))).thenReturn(taskEntity);

        // Настройка поведения для taskRepository
        when(taskRepository.save(any(Task.class))).thenReturn(taskEntity);

        // Настройка возврата DTO (если метод saveTask возвращает TaskDTO)
        when(taskMapper.toDTO(any(Task.class))).thenReturn(dto);

        // Вызов метода
//        TaskDTO savedDto = taskService.saveTask(dto);

        // Проверка
//        assertNotNull(savedDto);
//        verify(taskRepository).save(any(Task.class));
    }

}
