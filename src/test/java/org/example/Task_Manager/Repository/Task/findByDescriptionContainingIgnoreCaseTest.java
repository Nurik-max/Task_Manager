package org.example.Task_Manager.Repository.Task;

import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
public class findByDescriptionContainingIgnoreCaseTest {

    @Autowired
    private TaskRepository taskRepository;
    @Test
    void shouldReturnPageOfTaskByDescription(){

        Task task1 = new Task();
        task1.setDescription("Make a presentation");

        Task task2 = new Task();
        task2.setDescription("Cook a diner");

        Task task3 = new Task();
        task3.setDescription("Make a cake");

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
