package org.example.Task_Manager.Sevice.Task;
import org.example.Task_Manager.DTO.tasks.TaskDTO;
import org.example.Task_Manager.DTO.tasks.request.AdminCreateTaskRequest;
import org.example.Task_Manager.DTO.tasks.request.CreateTaskRequest;
import org.example.Task_Manager.DTO.tasks.response.AdminResponse;
import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Model.UserRole;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Repository.TaskMapper;
import org.example.Task_Manager.Repository.TaskRepository;
import org.example.Task_Manager.Sevice.AdminTaskService;
import org.example.Task_Manager.details.WorkerDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

     AdminCreateTaskRequest adminCreateTaskRequest = new AdminCreateTaskRequest();
        adminCreateTaskRequest.setDescription("hello");

        AdminResponse adminResponse = new AdminResponse();

        Worker worker = new Worker();
        worker.setUsername("admin");
        worker.setUserRole(UserRole.ADMIN);
        WorkerDetails workerDetails = new WorkerDetails(worker);

        Task taskEntity = new Task();

        // Настройка поведения репозитория
        when(taskMapper.toEntity(any(AdminCreateTaskRequest.class))).thenReturn(taskEntity);

        // Настройка поведения для taskRepository
        when(taskRepository.save(any(Task.class))).thenReturn(taskEntity);

//         Настройка возврата DTO (если метод saveTask возвращает TaskDTO)
        when(taskMapper.toAdminResponse(any(Task.class))).thenReturn(adminResponse);

//         Вызов метода
       AdminResponse savedDto = adminTaskService.saveTask(adminCreateTaskRequest, workerDetails);

//         Проверка
        assertNotNull(savedDto);
        assertEquals(adminResponse, savedDto);
        verify(taskRepository).save(taskEntity);
        verify(taskRepository).save(any(Task.class));
    }

}
