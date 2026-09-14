package org.example.Task_Manager.Sevice.Worker;

import org.example.Task_Manager.DTO.workers.AdminCreateWorkerDTO;
import org.example.Task_Manager.DTO.workers.response.AdminWorkerResponse;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class WS_CreateWorkerTest {

    @InjectMocks
    private WorkerService workerService;

    @Mock
    private WorkerRepository workerRepository;

    @Mock
    private WorkerMapper workerMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void createWorker() {

        AdminCreateWorkerDTO adminCreateWorkerDTO = new AdminCreateWorkerDTO();
        adminCreateWorkerDTO.setPassword("password");

        Worker worker = new Worker();
        worker.setWorkerStatus(WorkerStatus.WORKS);

        Worker savedWorker = worker;

        AdminWorkerResponse adminWorkerResponse = new AdminWorkerResponse();

        when(workerMapper.adminCreateWorkerFromDTO(adminCreateWorkerDTO)).thenReturn(worker);
        when(passwordEncoder.encode(adminCreateWorkerDTO.getPassword())).thenReturn("encodedPassword");
        when(workerRepository.save(worker))
                .thenReturn(savedWorker);
        when(workerMapper.toAdminWorkerResponse(savedWorker)).thenReturn(adminWorkerResponse);

       AdminWorkerResponse response = workerService.createWorker(adminCreateWorkerDTO);



        assertEquals("encodedPassword", worker.getPassword());
        assertEquals(WorkerStatus.WORKS, worker.getWorkerStatus());
        assertEquals(response, adminWorkerResponse);


        verify(workerMapper).adminCreateWorkerFromDTO(adminCreateWorkerDTO);
        verify(passwordEncoder).encode("password");
        verify(workerRepository).save(worker);
        verify(workerMapper).toAdminWorkerResponse(savedWorker);
    }
    }
