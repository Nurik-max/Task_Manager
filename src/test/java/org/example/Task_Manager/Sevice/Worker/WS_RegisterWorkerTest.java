package org.example.Task_Manager.Sevice.Worker;

import org.example.Task_Manager.DTO.workers.CreateWorkerDTO;
import org.example.Task_Manager.Exceptions.ValidationException;
import org.example.Task_Manager.Model.UserRole;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WS_RegisterWorkerTest {

    @InjectMocks
    private WorkerService workerService;

    @Mock
    private WorkerRepository workerRepository;

    @Mock
    private WorkerMapper workerMapper;

   @Mock
   private BCryptPasswordEncoder passwordEncoder;

    @Test
    void RegisterWorker() {

        CreateWorkerDTO workerDTO = new CreateWorkerDTO();
        workerDTO.setPassword("password");
        workerDTO.setConfirmPassword("password");
        workerDTO.setUsername("username");

        Worker worker = new Worker();
        worker.setUserRole(UserRole.ADMIN);
        worker.setWorkerStatus(WorkerStatus.WORKS);
        worker.setPassword(passwordEncoder.encode(workerDTO.getPassword()));

        when(workerMapper.toEntity(workerDTO)).thenReturn(worker);

        workerService.register(workerDTO);

        verify(workerRepository).save(worker);
        verify(workerMapper).toEntity(workerDTO);
        assertEquals(WorkerStatus.WORKS, worker.getWorkerStatus());

    }

    @Test
    void shouldThrowExceptionWhenWorkerDTOIsNull() {

        CreateWorkerDTO workerDTO = null;

        assertThrows(NullPointerException.class, () -> {
            workerService.register(workerDTO);
        });
    }

    @Test
     void shouldThrowExceptionWhenWrongValidation() {

        CreateWorkerDTO dto= new CreateWorkerDTO();
        dto.setPassword("password");
        dto.setUsername("username");
        dto.setConfirmPassword("wrong");

        assertThrows(ValidationException.class, () ->
                workerService.register(dto));

        verifyNoInteractions(workerRepository, workerMapper, passwordEncoder);
     }

     @Test
     void shouldThrowExceptionWhenUsernameAlreadyExists() {

        CreateWorkerDTO dto= new CreateWorkerDTO();
        dto.setPassword("password");
        dto.setConfirmPassword("password");
        dto.setUsername("username");

        when(workerRepository.existsWorkerByUsername("username")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () ->
                workerService.register(dto));

        verify(workerRepository).existsWorkerByUsername("username");
        verifyNoInteractions(workerMapper, passwordEncoder);
        verify(workerRepository, never()).save(any());
     }

     @Test
     void register_shouldCreateAdminWhenNoWorkersExist() {

        CreateWorkerDTO dto= new CreateWorkerDTO();
        dto.setPassword("password");
        dto.setConfirmPassword("password");
        dto.setUsername("username");

        Worker worker = new Worker();

        when(workerRepository.existsWorkerByUsername("username")).thenReturn(false);

        when(workerRepository.count()).thenReturn(0L);

        when(workerMapper.toEntity(dto)).thenReturn(worker);

        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");

        workerService.register(dto);

        assertEquals(UserRole.ADMIN, worker.getUserRole());
        assertEquals("encodedPassword", worker.getPassword());

        verify(workerRepository).save(worker);
        verify(workerMapper).toEntity(dto);

     }

    @Test
    void register_shouldCreateUserWhenNoWorkersExist() {

        CreateWorkerDTO dto= new CreateWorkerDTO();
        dto.setPassword("password");
        dto.setConfirmPassword("password");
        dto.setUsername("username");

        Worker worker = new Worker();

        when(workerRepository.existsWorkerByUsername("username")).thenReturn(false);

        when(workerRepository.count()).thenReturn(1L);

        when(workerMapper.toEntity(dto)).thenReturn(worker);

        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");

        workerService.register(dto);

        assertEquals(UserRole.USER, worker.getUserRole());
        assertEquals("encodedPassword", worker.getPassword());

        verify(workerRepository).save(worker);
        verify(workerMapper).toEntity(dto);

    }
}