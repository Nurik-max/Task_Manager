package org.example.Task_Manager.Sevice.Task;

import org.example.Task_Manager.Exceptions.WorkerNotFoundException;
import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Repository.TaskRepository;
import org.example.Task_Manager.Repository.WorkerRepository;
import org.example.Task_Manager.Sevice.AdminTaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ATS_GetTasksByWorkerIdTest {

    @InjectMocks
    private AdminTaskService adminTaskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private WorkerRepository workerRepository;

    @Test
    void shouldReturnTasksByWorkerId(){
        int workerId = 1;
        Worker worker = new Worker();
        worker.setId(workerId);
        List<Task> tasks = List.of(new Task());

        when(workerRepository.findById(workerId)).thenReturn(Optional.of(worker));

        when(taskRepository.findByWorker_Id(workerId))
                .thenReturn(tasks);

        List<Task> list = adminTaskService.getTasksByWorkerId(workerId);

        assertEquals(tasks,list);
        verify(taskRepository).findByWorker_Id(workerId);

    }
    @Test
    void shouldThrowExceptionWhenWorkerNotFound() {
        int workerId = 999;

        when(workerRepository.findById(workerId))
                .thenReturn(Optional.empty());

        assertThrows(
                WorkerNotFoundException.class,
                () -> adminTaskService.getTasksByWorkerId(workerId)
        );

        verify(workerRepository).findById(workerId);

        verifyNoInteractions(taskRepository);
    }
}
