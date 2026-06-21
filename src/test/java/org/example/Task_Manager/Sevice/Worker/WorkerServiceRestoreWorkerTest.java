package org.example.Task_Manager.Sevice.Worker;

import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Model.WorkerStatus;
import org.example.Task_Manager.Repository.WorkerRepository;
import org.example.Task_Manager.Sevice.WorkerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class WorkerServiceRestoreWorkerTest {

    @InjectMocks
    private WorkerService workerService;

    @Mock
    private WorkerRepository workerRepository;

    @Test
    void restoreWorker() {

        //Arrange
        Worker worker = new Worker();
        worker.setId(1);
        worker.setWorkerStatus(WorkerStatus.FIRED);

        //Mock
        when(workerRepository.findById(1)).thenReturn(Optional.of(worker));
        //Act
        workerService.restoreWorker(1);

        //Assert
        verify(workerRepository).findById(1);
        assertEquals(WorkerStatus.WORKS, worker.getWorkerStatus());

    }
}