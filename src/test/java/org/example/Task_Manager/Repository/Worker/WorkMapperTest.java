package org.example.Task_Manager.Repository.Worker;


import org.example.Task_Manager.DTO.workers.AdminCreateWorkerDTO;
import org.example.Task_Manager.DTO.workers.CreateWorkerDTO;
import org.example.Task_Manager.DTO.workers.UpdateWorkerDTO;
import org.example.Task_Manager.DTO.workers.request.ProfileUpdateDTO;
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
import static org.junit.jupiter.api.Assertions.assertNull;


public class WorkMapperTest {


    private final WorkerMapper workerMapper = Mappers.getMapper(WorkerMapper.class);

    @Test
    void testToUserResponse(){

        Worker worker = new Worker();
        worker.setUsername("William");
        worker.setSurname("Smith");
        worker.setEmail("will@example.com");
        worker.setPosition("Developer");


        WorkerDTO workerDTO = workerMapper.toUserResponse(worker);

        assertEquals("William", workerDTO.getUsername());
        assertEquals("Smith", workerDTO.getSurname());
        assertEquals("will@example.com", workerDTO.getEmail());
        assertEquals("Developer", workerDTO.getPosition());

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

        assertEquals("William", worker.getUsername());
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

    @Test
    void updateProfileFromDTO(){

        Worker worker = new Worker();
        worker.setUsername("William");
        worker.setSurname("Smith");
        worker.setEmail("will@.com");

        ProfileUpdateDTO dto = new ProfileUpdateDTO();
        dto.setUsername(null);
        dto.setSurname(null);
        dto.setEmail("smith@.com");

        workerMapper.updateProfileFromDTO(dto,worker);

        assertEquals("William", worker.getUsername());
        assertEquals("Smith", worker.getSurname());
        assertEquals("smith@.com", worker.getEmail());

    }

    @Test
    void  adminCreateWorkerFromDTO(){

        AdminCreateWorkerDTO dto = new AdminCreateWorkerDTO();
        dto.setUsername("William");

       Worker worker = workerMapper.adminCreateWorkerFromDTO(dto);

        assertEquals("William", worker.getUsername());
        assertEquals(WorkerStatus.WORKS, worker.getWorkerStatus());
        assertNull(worker.getUserRole());
    }

    @Test
    void toAdminWorkerResponse(){

        Worker worker3 = new Worker();
        worker3.setId(3);
        worker3.setUsername("Micael");
        worker3.setSurname("Walter");
        worker3.setPosition("engineer 2");
        worker3.setEmail("3@.com");
        worker3.setPassword("1234");
        worker3.setPhone("1234567890");
        worker3.setUserRole(UserRole.USER);
        worker3.setCreatedDate(LocalDateTime.of(2026, 8, 23, 10, 0));

        AdminWorkerResponse workerDTO = workerMapper.toAdminWorkerResponse(worker3);

        assertEquals("Micael", workerDTO.getUsername());
        assertEquals("Walter", workerDTO.getSurname());
        assertEquals("engineer 2", workerDTO.getPosition());
        assertEquals("3@.com", workerDTO.getEmail());
        assertEquals("1234", workerDTO.getPassword());
        assertEquals(LocalDateTime.of(2026, 8, 23, 10, 0), workerDTO.getCreatedAt());
    }

    @Test
    void toProfileUpdateDTO() {
        Worker worker = new Worker();

        worker.setUsername("William");
        worker.setSurname("Smith");
        worker.setEmail("will@example.com");
        worker.setPhone("123456789");

        ProfileUpdateDTO dto =
                workerMapper.toProfileUpdateDTO(worker);

        assertEquals("William", dto.getUsername());
        assertEquals("Smith", dto.getSurname());
        assertEquals("will@example.com", dto.getEmail());
        assertEquals("123456789", dto.getPhone());
    }
}
