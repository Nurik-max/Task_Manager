package org.example.Task_Manager.Sevice.Task;

import org.example.Task_Manager.DTO.tasks.response.AdminResponse;
import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Repository.TaskMapper;
import org.example.Task_Manager.Repository.TaskRepository;
import org.example.Task_Manager.Sevice.AdminTaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
//@ActiveProfiles("test")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AdminTaskServiceGetTasksTest {

    @InjectMocks
    private AdminTaskService adminTaskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;
    @Test
    void shouldReturnTasksPage() {

        // Arrange
        Pageable pageable = PageRequest.of(0, 5);

        Task task = new Task();
        AdminResponse dto = new AdminResponse();

        Page<Task> taskPage = new PageImpl<>(List.of(task));

        when(taskRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(taskPage);

        when(taskMapper.toAdminResponse(task))
                .thenReturn(dto);

        // Act
        Page<AdminResponse> result = adminTaskService.getTasks(
                null, null,null, null, null, null, false, pageable
        );

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(dto, result.getContent().get(0));
        verify(taskRepository).findAll( any(Specification.class), eq(pageable));
        verify(taskMapper).toAdminResponse(task);
    }
}