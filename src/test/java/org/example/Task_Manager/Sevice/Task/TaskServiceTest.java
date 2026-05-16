package org.example.Task_Manager.Sevice.Task;

import org.example.Task_Manager.Exceptions.WorkerNotFoundException;
import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Repoitory.TaskRepository;
import org.example.Task_Manager.Repoitory.WorkerRepository;
import org.example.Task_Manager.Sevice.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private WorkerRepository workerRepository;

    @Test
    void shouldReturnTasksWhenWorkerExists(){
        int workerId = 1;
        Worker worker = new Worker();
        List<Task> tasks = List.of(new Task());

        when(workerRepository.findById(workerId))
                .thenReturn(Optional.of(worker));

        when(taskRepository.findByWorker(worker))
                .thenReturn(tasks);

//        List<Task> result = taskService.workerListOfTask(workerId);
//
//        assertEquals(tasks, result);

        verify(workerRepository).findById(workerId);
        verify(taskRepository).findByWorker(worker);
    }
    @Test
    void shouldThrowExceptionWhenWorkerNotFound(){
        int workerId = 999;

        when(workerRepository.findById(workerId))
                .thenReturn(Optional.empty());

//        assertThrows(WorkerNotFoundException.class, () -> {
//            taskService.workerListOfTask(workerId);
//        });

        verify(workerRepository).findById(workerId);
    }
}
