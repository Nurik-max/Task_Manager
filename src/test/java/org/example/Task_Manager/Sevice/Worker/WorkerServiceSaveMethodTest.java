package org.example.Task_Manager.Sevice.Worker;

import org.example.Task_Manager.DTO.workers.response.WorkerDTO;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Repository.WorkerMapper;
import org.example.Task_Manager.Repository.WorkerRepository;
import org.example.Task_Manager.Sevice.WorkerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WorkerServiceSaveMethodTest {

    @InjectMocks
    private WorkerService workerService;

    @Mock
    private WorkerRepository workerRepository;

    @Mock
    private WorkerMapper workerMapper;

    @Test
    void saveWorker() {

        WorkerDTO workerDTO = new WorkerDTO();
//        workerDTO.setWorkerStatus(null);

        Worker worker = new Worker();
//
//        when(workerMapper.toEntity(workerDTO)).thenReturn(worker);
//
//        workerService.saveWorker(workerDTO);
//
//        verify(workerRepository).save(worker);
//        verify(workerMapper).toEntity(workerDTO);
//        assertEquals(WorkerStatus.WORKS, worker.getWorkerStatus());

    }

    @Test
    void shouldThrowExceptionWhenWorkerDTOIsNull() {

        WorkerDTO workerDTO = null;

//        assertThrows(IllegalArgumentException.class, () -> {
//            workerService.saveWorker(workerDTO);
//        });
    }
}