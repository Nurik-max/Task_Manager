package org.example.Task_Manager.Repository.Worker;

import org.example.Task_Manager.Model.UserRole;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Repository.WorkerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WorkerRepositoryTest {

    @Autowired
    private WorkerRepository repository;

    @Test
    void findByUsername() {

        Worker worker1 = new Worker();
        worker1.setUsername("Max");
        worker1.setSurname("Gilbert");
        worker1.setPosition("HR");
        worker1.setEmail("1@.com");
        worker1.setPassword("1234");
        worker1.setPhone("1234567890");
        worker1.setUserRole(UserRole.USER);

        Worker worker2 = new Worker();
        worker2.setUsername("John");
        worker2.setSurname("Mustermann");
        worker2.setPosition("engineer");
        worker2.setEmail("2@.com");
        worker2.setPassword("1234");
        worker2.setPhone("1234567890");
        worker2.setUserRole(UserRole.USER);

        Worker worker3 = new Worker();
        worker3.setUsername("Micael");
        worker3.setSurname("Walter");
        worker3.setPosition("engineer 2");
        worker3.setEmail("3@.com");
        worker3.setPassword("1234");
        worker3.setPhone("1234567890");
        worker3.setUserRole(UserRole.USER);

        repository.save(worker1);
        repository.save(worker2);
        repository.save(worker3);

        repository.flush();

       Optional<Worker> worker = repository.findByUsername("John");

       assertTrue(worker.isPresent());
       assertNotNull(worker.get());
       assertEquals("John", worker.get().getUsername());

    }

    @Test
    void findBySurname() {

        Worker worker1 = new Worker();
        worker1.setUsername("Max");
        worker1.setSurname("Gilbert");
        worker1.setPosition("HR");
        worker1.setEmail("1@.com");
        worker1.setPassword("1234");
        worker1.setPhone("1234567890");
        worker1.setUserRole(UserRole.USER);

        Worker worker2 = new Worker();
        worker2.setUsername("John");
        worker2.setSurname("Mustermann");
        worker2.setPosition("engineer");
        worker2.setEmail("2@.com");
        worker2.setPassword("1234");
        worker2.setPhone("1234567890");
        worker2.setUserRole(UserRole.USER);

        Worker worker3 = new Worker();
        worker3.setUsername("Micael");
        worker3.setSurname("Walter");
        worker3.setPosition("engineer 2");
        worker3.setEmail("3@.com");
        worker3.setPassword("1234");
        worker3.setPhone("1234567890");
        worker3.setUserRole(UserRole.USER);

        repository.save(worker1);
        repository.save(worker2);
        repository.save(worker3);

        repository.flush();

        List<Worker> workers = repository.findBySurname("Walter");

        assertFalse(workers.isEmpty());
        assertNotNull(workers.get(0));
        assertEquals("Micael", workers.get(0).getUsername());
        assertEquals("Walter", workers.get(0).getSurname());
    }

    @Test
    void findByPosition() {

        Worker worker1 = new Worker();
        worker1.setUsername("Max");
        worker1.setSurname("Gilbert");
        worker1.setPosition("HR");
        worker1.setEmail("1@.com");
        worker1.setPassword("1234");
        worker1.setPhone("1234567890");
        worker1.setUserRole(UserRole.USER);

        Worker worker2 = new Worker();
        worker2.setUsername("John");
        worker2.setSurname("Mustermann");
        worker2.setPosition("engineer");
        worker2.setEmail("2@.com");
        worker2.setPassword("1234");
        worker2.setPhone("1234567890");
        worker2.setUserRole(UserRole.USER);

        Worker worker3 = new Worker();
        worker3.setUsername("Micael");
        worker3.setSurname("Walter");
        worker3.setPosition("engineer 2");
        worker3.setEmail("3@.com");
        worker3.setPassword("1234");
        worker3.setPhone("1234567890");
        worker3.setUserRole(UserRole.USER);

        repository.save(worker1);
        repository.save(worker2);
        repository.save(worker3);

        repository.flush();

        List<Worker> workers = repository.findByPosition("engineer");

        assertFalse(workers.isEmpty());
        assertNotNull(workers.get(0));
        assertEquals("engineer", workers.get(0).getPosition());
    }

    @Test
    void existsWorkerById() {

        Worker worker1 = new Worker();
        worker1.setUsername("Max");
        worker1.setSurname("Gilbert");
        worker1.setPosition("HR");
        worker1.setEmail("1@.com");
        worker1.setPassword("1234");
        worker1.setPhone("1234567890");
        worker1.setUserRole(UserRole.USER);

        Worker worker24 = new Worker();
        worker24.setUsername("John");
        worker24.setSurname("Garden");
        worker24.setPosition("engineer");
        worker24.setEmail("24@.com");
        worker24.setPassword("1234");
        worker24.setPhone("1234567890");
        worker24.setUserRole(UserRole.USER);

        Worker worker3 = new Worker();
        worker3.setUsername("Micael");
        worker3.setSurname("Walter");
        worker3.setPosition("engineer 2");
        worker3.setEmail("3@.com");
        worker3.setPassword("1234");
        worker3.setPhone("1234567890");
        worker3.setUserRole(UserRole.USER);

        repository.save(worker1);
        repository.save(worker24);
        repository.save(worker3);

        repository.flush();

       Boolean result = repository.existsWorkerById(worker24.getId());

        assertTrue(result);
    }

    @Test
    void existsWorkerByUsername() {

        Worker worker1 = new Worker();
        worker1.setId(1);
        worker1.setUsername("Max");
        worker1.setSurname("Gilbert");
        worker1.setPosition("HR");
        worker1.setEmail("1@.com");
        worker1.setPassword("1234");
        worker1.setPhone("1234567890");
        worker1.setUserRole(UserRole.USER);

        Worker worker2 = new Worker();
        worker2.setId(2);
        worker2.setUsername("John");
        worker2.setSurname("Mustermann");
        worker2.setPosition("engineer");
        worker2.setEmail("2@.com");
        worker2.setPassword("1234");
        worker2.setPhone("1234567890");
        worker2.setUserRole(UserRole.USER);

        Worker worker3 = new Worker();
        worker3.setId(3);
        worker3.setUsername("Micael");
        worker3.setSurname("Walter");
        worker3.setPosition("engineer 2");
        worker3.setEmail("3@.com");
        worker3.setPassword("1234");
        worker3.setPhone("1234567890");
        worker3.setUserRole(UserRole.USER);

        repository.save(worker1);
        repository.save(worker2);
        repository.save(worker3);

        repository.flush();

        Boolean result = repository.existsWorkerByUsername("Max");

        assertTrue(result);
    }

    @Test
    void findByIdNot() {

        Worker worker1 = new Worker();
        worker1.setId(1);
        worker1.setUsername("Max");
        worker1.setSurname("Gilbert");
        worker1.setPosition("HR");
        worker1.setEmail("1@.com");
        worker1.setPassword("1234");
        worker1.setPhone("1234567890");
        worker1.setUserRole(UserRole.USER);

        Worker worker2 = new Worker();
        worker2.setId(2);
        worker2.setUsername("John");
        worker2.setSurname("Mustermann");
        worker2.setPosition("engineer");
        worker2.setEmail("2@.com");
        worker2.setPassword("1234");
        worker2.setPhone("1234567890");
        worker2.setUserRole(UserRole.USER);

        Worker worker3 = new Worker();
        worker3.setId(3);
        worker3.setUsername("Micael");
        worker3.setSurname("Walter");
        worker3.setPosition("engineer 2");
        worker3.setEmail("3@.com");
        worker3.setPassword("1234");
        worker3.setPhone("1234567890");
        worker3.setUserRole(UserRole.USER);

        repository.save(worker1);
        repository.save(worker2);
        repository.save(worker3);

        repository.flush();

       List<Worker> workers = repository.findByIdNot(1);

       assertFalse(workers.isEmpty());
       assertFalse(workers.contains(worker1));
    }
}