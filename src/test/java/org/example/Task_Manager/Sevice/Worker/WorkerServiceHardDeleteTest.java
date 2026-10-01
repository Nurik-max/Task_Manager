package org.example.Task_Manager.Sevice.Worker;

import org.example.Task_Manager.DTO.workers.response.AdminWorkerResponse;
import org.example.Task_Manager.Exceptions.WorkerNotFoundException;
import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Model.UserRole;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Repository.TaskRepository;
import org.example.Task_Manager.Repository.WorkerMapper;
import org.example.Task_Manager.Repository.WorkerRepository;
import org.example.Task_Manager.Sevice.AdminTaskService;
import org.example.Task_Manager.Sevice.WorkerService;
import org.example.Task_Manager.details.WorkerDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    @Mock
    private WorkerMapper workerMapper;

    @Test
    void hardDeleteWorker() {

        //Arrange
        Worker worker = new Worker();
        worker.setUserRole(UserRole.ADMIN);

        int oldWorker = 1;
        Integer newWorker = 2;

        AdminWorkerResponse expectedResponse = new AdminWorkerResponse();

        //Mock
        when(workerRepository.findById(oldWorker)).thenReturn(Optional.of(worker));
        when(workerMapper.toAdminWorkerResponse(worker)).thenReturn(expectedResponse);

        //act
      AdminWorkerResponse response = workerService.hardDeleteWorker(oldWorker, newWorker);

        //Assert
       assertSame(expectedResponse, response);

        verify(adminTaskService)
                .taskReassignment(oldWorker, newWorker);

        verify(workerRepository)
                .delete(worker);

        verify(workerMapper).toAdminWorkerResponse(worker);

    }

    @Test
    void whenOldWorkerEqualNewWorker(){

        int oldWorker = 1;
        Integer newWorker = 1;

        assertThrows(IllegalArgumentException.class,
                () -> workerService.hardDeleteWorker(oldWorker, newWorker));

        verifyNoInteractions(workerRepository,adminTaskService,workerMapper);
    }

    @Test
    void whenWorkerNotFound(){
        Worker worker = new Worker();

        int oldWorker = 1;
        Integer newWorker = null;

        when(workerRepository.findById(oldWorker)).thenReturn(Optional.empty());

        assertThrows(WorkerNotFoundException.class,
                ()-> workerService.hardDeleteWorker(oldWorker, newWorker));

        verifyNoInteractions(adminTaskService,workerMapper);
        verify(workerRepository, never()).delete(worker);
    }
}