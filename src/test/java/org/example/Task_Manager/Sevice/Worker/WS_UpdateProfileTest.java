package org.example.Task_Manager.Sevice.Worker;

import org.example.Task_Manager.DTO.workers.UpdateWorkerDTO;
import org.example.Task_Manager.DTO.workers.request.ProfileUpdateDTO;
import org.example.Task_Manager.DTO.workers.response.WorkerDTO;
import org.example.Task_Manager.Exceptions.WorkerNotFoundException;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Repository.WorkerMapper;
import org.example.Task_Manager.Repository.WorkerRepository;
import org.example.Task_Manager.Sevice.WorkerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class WS_UpdateProfileTest {

        @InjectMocks
        private WorkerService workerService;

        @Mock
        private WorkerRepository workerRepository;

        @Mock
        private WorkerMapper workerMapper;

        @Mock
        private BCryptPasswordEncoder passwordEncoder;

    @Test
    void updateProfile() {

        int workerId = 1;

        Worker existingWorker  = new Worker();
        existingWorker.setId(workerId);

        ProfileUpdateDTO profileUpdateDTO = new ProfileUpdateDTO();

        WorkerDTO workerDTO = new WorkerDTO();

        when(workerRepository.findById(workerId)).thenReturn(Optional.of(existingWorker));
        when(workerRepository.save(existingWorker)).thenReturn(existingWorker);
        when(workerMapper.toUserResponse(existingWorker)).thenReturn(workerDTO);

       WorkerDTO result = workerService.updateProfile(workerId, profileUpdateDTO);

        assertSame(workerDTO, result);
        verify(workerRepository).findById(workerId);
        verify(workerRepository).save(existingWorker);
        verify(workerMapper).toUserResponse(existingWorker);
        verify(workerMapper).updateProfileFromDTO(profileUpdateDTO, existingWorker);


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