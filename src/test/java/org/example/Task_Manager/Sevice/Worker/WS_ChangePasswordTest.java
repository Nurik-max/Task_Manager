package org.example.Task_Manager.Sevice.Worker;

import org.example.Task_Manager.DTO.workers.ChangePasswordDTO;
import org.example.Task_Manager.Exceptions.IncorrectPasswordException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WS_ChangePasswordTest {


    @InjectMocks
    private WorkerService workerService;

    @Mock
    private WorkerRepository workerRepository;

    @Mock
    private WorkerMapper workerMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;


    @Test
    void changePassword() {

        int workerId = 1;

        ChangePasswordDTO  dto = new ChangePasswordDTO();
        dto.setOldPassword("1234");
        dto.setNewPassword("9876");
        dto.setConfirmPassword("1234");

        Worker worker = new Worker();
        worker.setId(workerId);
        worker.setPassword("encodedOldPassword");



        when(workerRepository.findById(workerId)).thenReturn(Optional.of(worker));
        when(passwordEncoder.matches(dto.getOldPassword(), worker.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(dto.getNewPassword())).thenReturn("encodedNewPassword");

        workerService.changePassword(workerId, dto);

        assertEquals("encodedNewPassword", worker.getPassword());

        verify(workerRepository).findById(workerId);
        verify(passwordEncoder).matches(dto.getOldPassword(), "encodedOldPassword");
        verify(passwordEncoder).encode("9876");


    }

    @Test
    void incorrectPassword() {

        int workerId = 1;
        ChangePasswordDTO dto = new ChangePasswordDTO();
        dto.setOldPassword("1234");

        Worker worker = new Worker();
        worker.setId(workerId);
        worker.setPassword("wrongOldPassword");

        when(workerRepository.findById(workerId)).thenReturn(Optional.of(worker));
        when(passwordEncoder.matches(dto.getOldPassword(), worker.getPassword())).thenReturn(false);

        assertThrows(IncorrectPasswordException.class, () -> workerService.changePassword(1, dto));

        verify(workerRepository).findById(workerId);
        verify(passwordEncoder).matches(dto.getOldPassword(), "wrongOldPassword");
        verify(passwordEncoder, never()).encode(anyString());


    }

    @Test
    void workerNotFound() {
        int workerId = 999;

        when(workerRepository.findById(workerId)).thenReturn(Optional.empty());

        assertThrows(WorkerNotFoundException.class,  () -> workerService.changePassword(workerId, new ChangePasswordDTO()));

        verify(workerRepository).findById(workerId);
        verifyNoInteractions(passwordEncoder);
    }
}