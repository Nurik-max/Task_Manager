package org.example.Task_Manager.Sevice.Worker;

import org.example.Task_Manager.DTO.workers.UpdateWorkerDTO;
import org.example.Task_Manager.DTO.workers.response.AdminWorkerResponse;
import org.example.Task_Manager.Exceptions.WorkerNotFoundException;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Repository.WorkerMapper;
import org.example.Task_Manager.Repository.WorkerRepository;
import org.example.Task_Manager.Sevice.WorkerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WS_UpdateWorkerTest {


    @InjectMocks
    private WorkerService workerService;

    @Mock
    private WorkerRepository workerRepository;

   @Mock
    private WorkerMapper workerMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;



    @Test
    void updateWorker() {

        int workerId = 1;

        UpdateWorkerDTO updateWorkerDTO = new UpdateWorkerDTO();

        Worker existingWorker = new Worker();
        existingWorker.setId(workerId);


        AdminWorkerResponse response = new AdminWorkerResponse();

        when(workerRepository.findById(workerId)).thenReturn(Optional.of(existingWorker));
        when(workerRepository.save(existingWorker)).thenReturn(existingWorker);
        when(workerMapper.toAdminWorkerResponse(existingWorker)).thenReturn(response);

        AdminWorkerResponse result = workerService.updateWorker(workerId, updateWorkerDTO);

        assertSame(response, result);
        verify(workerRepository).findById(workerId);
        verify(workerRepository).save(existingWorker);
        verify(workerMapper).toAdminWorkerResponse(existingWorker);
        verify(workerMapper).updateWorkerFromDTO(updateWorkerDTO, existingWorker);

    }

    @Test
    void shouldHandleWorkerNotFoundException(){

        int workerId = 999;

        when(workerRepository.findById(workerId)).thenReturn(Optional.empty());

       assertThrows(WorkerNotFoundException.class, ()-> {
                workerService.updateWorker(workerId, new UpdateWorkerDTO());
       });

       verify(workerRepository).findById(workerId);
       verifyNoInteractions(workerMapper);
       verify(workerRepository, never()).save(any());
    }
}