package org.example.Task_Manager.Sevice.Worker;

import org.example.Task_Manager.DTO.workers.response.AdminWorkerResponse;
import org.example.Task_Manager.Exceptions.WorkerNotFoundException;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Model.WorkerStatus;
import org.example.Task_Manager.Repository.WorkerMapper;
import org.example.Task_Manager.Repository.WorkerRepository;
import org.example.Task_Manager.Sevice.WorkerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkerServiceRestoreWorkerTest {

    @InjectMocks
    private WorkerService workerService;

    @Mock
    private WorkerRepository workerRepository;

    @Mock
    private WorkerMapper workerMapper;

    @Test
    void restoreWorker() {

        //Arrange
        Worker worker = new Worker();
        worker.setId(1);
        worker.setWorkerStatus(WorkerStatus.FIRED);

        AdminWorkerResponse expected = new AdminWorkerResponse();

        //Mock
        when(workerRepository.findById(1)).thenReturn(Optional.of(worker));
        when(workerMapper.toAdminWorkerResponse(worker)).thenReturn(expected);
        //Act
       AdminWorkerResponse response = workerService.restoreWorker(1);

        //Assert
        verify(workerRepository).findById(1);
        assertEquals(expected, response);
        assertEquals(WorkerStatus.WORKS, worker.getWorkerStatus());

    }

    @Test
    void workerNotFound(){

        int workerId = 999;

        when(workerRepository.findById(workerId)).thenReturn(Optional.empty());

        assertThrows(WorkerNotFoundException.class, () -> workerService.restoreWorker(workerId));

        verify(workerRepository).findById(workerId);
        verifyNoInteractions(workerMapper);

    }
}