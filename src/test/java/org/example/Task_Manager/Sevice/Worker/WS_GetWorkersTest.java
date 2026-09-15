package org.example.Task_Manager.Sevice.Worker;

import org.example.Task_Manager.DTO.workers.response.AdminWorkerResponse;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Model.WorkerStatus;
import org.example.Task_Manager.Repository.WorkerMapper;
import org.example.Task_Manager.Repository.WorkerRepository;
import org.example.Task_Manager.Sevice.WorkerService;
import org.example.Task_Manager.specification.WorkerSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class WS_GetWorkersTest {

    @InjectMocks
    private WorkerService workerService;

    @Mock
    private WorkerRepository workerRepository;

    @Mock
    private WorkerMapper workerMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void getWorkers() {

        Pageable pageable = PageRequest.of(0, 10);
//        Specification<Worker> spec = Specification.where(WorkerSpecification.isNotFired());

        Worker worker = new Worker();

        AdminWorkerResponse response = new AdminWorkerResponse();

        Page<Worker> page = new PageImpl<>(List.of(worker));

        when(workerRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
        when(workerMapper.toAdminWorkerResponse(worker)).thenReturn(response);

        Page<AdminWorkerResponse> result  = workerService.getWorkers(null, null, null, null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(response, result.getContent().get(0));
        verify(workerRepository).findAll( any(Specification.class), eq(pageable));
        verify(workerMapper).toAdminWorkerResponse(worker);


    }
}