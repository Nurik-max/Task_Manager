package org.example.Task_Manager.Repository.Worker;


import org.example.Task_Manager.DTO.workers.CreateWorkerDTO;
import org.example.Task_Manager.DTO.workers.UpdateWorkerDTO;
import org.example.Task_Manager.DTO.workers.response.AdminWorkerResponse;
import org.example.Task_Manager.DTO.workers.response.WorkerDTO;
import org.example.Task_Manager.Model.Status;
import org.example.Task_Manager.Model.UserRole;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Model.WorkerStatus;
import org.example.Task_Manager.Repository.WorkerMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class WorkMapperTest {


    private final WorkerMapper workerMapper = Mappers.getMapper(WorkerMapper.class);

    @Test
    void testToUserResponse(){

        Worker worker = new Worker();
        worker.setUsername("William");

        WorkerDTO workerDTO = workerMapper.toUserResponse(worker);

        assertEquals("William", workerDTO.getUsername());

    }

    @Test
    void testToAdminResponse(){

        Worker worker = new Worker();
        worker.setUsername("Bob");

        AdminWorkerResponse workerDTO = workerMapper.toAdminWorkerResponse(worker);

        assertEquals("Bob", workerDTO.getUsername());

    }

    @Test
    void testToEntity(){
        CreateWorkerDTO dto = new CreateWorkerDTO();
        dto.setUsername("William");

       Worker worker =  workerMapper.toEntity(dto);

        assertEquals("William", dto.getUsername());
        assertEquals(UserRole.USER,worker.getUserRole());
        assertEquals(WorkerStatus.WORKS,worker.getWorkerStatus());
    }

    @Test
    void toAdminWorkerResponseShouldMapIdAndCreatedDate() {

        Worker worker = new Worker();
        worker.setId(1);

        LocalDateTime createdDate = LocalDateTime.of(
                2026, 8, 23, 10, 0
        );

        worker.setCreatedDate(createdDate);

        AdminWorkerResponse response =
                workerMapper.toAdminWorkerResponse(worker);

        assertEquals(1, response.getId());
        assertEquals(createdDate, response.getCreatedAt());
    }

    @Test
    void updateWorkerFromDTO(){

        Worker worker = new Worker();
        worker.setUsername("William");
        worker.setSurname("Smith");

        UpdateWorkerDTO  dto = new UpdateWorkerDTO();
        dto.setUsername(null);
        dto.setSurname("Lincoln");

        workerMapper.updateWorkerFromDTO(dto,worker);

        assertEquals("William", worker.getUsername());
        assertEquals("Lincoln", worker.getSurname());

    }

}
