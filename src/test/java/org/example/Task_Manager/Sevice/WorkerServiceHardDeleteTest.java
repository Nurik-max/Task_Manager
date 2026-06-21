package org.example.Task_Manager.Sevice;

import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Repoitory.TaskRepository;
import org.example.Task_Manager.Repoitory.WorkerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class WorkerServiceHardDeleteTest {

    @InjectMocks
    private WorkerService workerService;

    @Mock
    private AdminTaskService adminTaskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private WorkerRepository workerRepository;

    @Test
    void hardDeleteWorker() {

        //Arrange
        Worker worker = new Worker();
        Worker newWorkerEntity = new Worker();
        Task task1 = new Task();
        List<Task> taskList = List.of(task1);
        int oldWorker = 1;
        Integer newWorker = 2;

        //Mock
        when(workerRepository.findById(oldWorker)).thenReturn(Optional.of(worker));


//        //Spy
//        TaskService spyService = spy(taskService);
//        spyService.taskReassignment(oldWorker, newWorker);

        //act
        workerService.hardDeleteWorker(oldWorker, newWorker);

        //Assert
        verify(adminTaskService)
                .taskReassignment(oldWorker, newWorker);

        verify(workerRepository)
                .delete(worker);

    }
}