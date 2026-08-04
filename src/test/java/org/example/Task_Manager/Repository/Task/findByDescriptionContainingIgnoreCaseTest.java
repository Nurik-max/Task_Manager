package org.example.Task_Manager.Repository.Task;

import org.example.Task_Manager.Model.Priority;
import org.example.Task_Manager.Model.Status;
import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class findByDescriptionContainingIgnoreCaseTest {

    @Autowired
    private TaskRepository taskRepository;
    @Test
    void shouldReturnPageOfTaskByDescription(){

        taskRepository.deleteAll();
        taskRepository.flush();

        Task task1 = new Task();
        task1.setDescription("Make a presentation");
        task1.setPriority(Priority.HIGH);
        task1.setStatus(Status.NEW);

        Task task2 = new Task();
        task2.setDescription("Cook a diner");
        task2.setPriority(Priority.HIGH);
        task2.setStatus(Status.NEW);

        Task task3 = new Task();
        task3.setDescription("Make a cake");
        task3.setPriority(Priority.HIGH);
        task3.setStatus(Status.NEW);

        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);
        taskRepository.flush();

        Pageable pageable = PageRequest.of(0, 2);


        Page<Task> res = taskRepository.findByDescriptionContainingIgnoreCase("make", pageable);

        Assertions.assertEquals(2, res.getContent().size());
        Assertions.assertEquals(2, res.getTotalElements());
        Assertions.assertEquals(1, res.getTotalPages());


    }

}
