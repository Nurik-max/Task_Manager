package org.example.Task_Manager.Sevice.Task;

import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Repoitory.TaskRepository;
import org.example.Task_Manager.Repoitory.WorkerRepository;
import org.example.Task_Manager.Sevice.AdminTaskService;
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
class AdminTaskServiceReassignmentTest {

    @InjectMocks
    private AdminTaskService adminTaskService;

    @Mock
    private WorkerRepository workerRepository;

    @Mock
    private TaskRepository taskRepository;

    @Test
    void taskReassignment() {
        //Arrange
        int newWorker = 1;
        int oldWorker = 2;

        Worker new_Worker = new Worker();

        Task task1 = new Task();
        Task task2 = new Task();

        List<Task> taskList = (List.of(task1, task2));

        // mock repository
        when(workerRepository.findById(newWorker)).thenReturn(Optional.of(new_Worker));
        when(workerRepository.existsWorkerById(oldWorker))
                .thenReturn(true);
        when(taskRepository.findByWorker_Id(oldWorker)).thenReturn(taskList);

        //Act
        adminTaskService.taskReassignment(oldWorker, newWorker);

        //Assert
        assertEquals(new_Worker, task1.getWorker());
        assertEquals(new_Worker, task2.getWorker());
        verify(workerRepository).findById(newWorker);
        verify(taskRepository).findByWorker_Id(oldWorker);

    }
    /*
    @ExtendWith(MockitoExtension.class)
class TaskServiceGetTaskTest{
    * @InjectMocks
    * private TaskService taskService;
    *
    * @Mock
    * TaskRepository taskRepository;
    *
    * @Mock
      TaskMapper taskMapper;
    *
    * @Test
    * void shouldGetTask(){
    *
    * //Arrange
        int task_Id = 1;
    * Task task = new Task();
      TaskDTO taskDTO = new TaskDTO();
    * task.setId(task_Id);
    *
    * //Mock
    * when(taskRepository.findById(task_Id)).thenReturn(Optional.of(task));
        when(taskMapper.toDTO(task)).thenReturn(taskDTO);
    *
    * //Act
    * TaskDTO result = taskService.getTask(task_Id);
    *
    * //Assert
        assertEquals(taskDTO, result )
    * verify(taskRepository).findById(task_Id);
      verify(taskMapper).toDTO(task);
    *
    * }
    }
    @Test
    void shouldThrowExceptionWhenTaskNotFound(){

    int task_id = 999;

    when(taskRepository.findById(task_id)).thenReturn(Optional.empty());

    assertThrow(TaskNotFoundException.class, () -> {
    taskService.getTask(task_id);
    });

    verify(taskRepository).findById(task_id);

    }*/
}