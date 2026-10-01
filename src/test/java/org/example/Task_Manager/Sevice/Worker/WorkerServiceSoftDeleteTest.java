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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkerServiceSoftDeleteTest {

    @Mock
    private WorkerRepository workerRepository;

    @InjectMocks
    private WorkerService workerService;

    @Mock
    private WorkerMapper workerMapper;

    @Test
    void shouldSoftDeleteWorker() {
        int workerID = 1;

        Worker worker = new Worker();
        worker.setId(workerID);
        worker.setWorkerStatus(WorkerStatus.WORKS);

        AdminWorkerResponse adminWorkerResponse = new AdminWorkerResponse();

        // 🔥 mock findById
        when(workerRepository.findById(workerID))
                .thenReturn(Optional.of(worker));
        when(workerMapper.toAdminWorkerResponse(worker)).thenReturn(adminWorkerResponse);

        // act
       AdminWorkerResponse response = workerService.softDeleteWorker(workerID);

        // assert
        verify(workerRepository).findById(workerID);
        assertEquals(WorkerStatus.FIRED, worker.getWorkerStatus());
        assertEquals(adminWorkerResponse, response);
    }

    @Test
    void shouldThrowExceptionWhenWorkerNotFound() {
        when(workerRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(WorkerNotFoundException.class, () -> {
            workerService.softDeleteWorker(1);
        });
    }
}