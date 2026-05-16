package org.example.Task_Manager.Sevice.Worker;

import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Repoitory.WorkerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class WorkerServiceTest {

    @Autowired
    private WorkerRepository workerRepository;

    @Test
    void setUp(){
        Worker worker = new Worker("William", "Lunghram", "programmer");
        workerRepository.save(worker);

        assertEquals("William", worker.getName());
        assertEquals("Lunghram", worker.getSurname());
        assertEquals("programmer", worker.getPosition());
    }


}
