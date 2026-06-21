package org.example.Task_Manager.Repository.Task;

import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class FindByDescriptionTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void shouldReturnDescription(){

    Task task1 = new Task();
        task1.setDescription("Learn Integration test");

        Task task2 = new Task();
        task2.setDescription("Write One connect list");

        Task task3 = new Task();
        task3.setDescription("learn data structure");

        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);
        taskRepository.flush();
       List<Task>  taskList =  taskRepository.findByDescriptionContainingIgnoreCase("learn");

        Assertions.assertNotNull(taskList);
        assertThat(taskList).hasSize(2)
                .extracting(Task::getDescription)
                .allSatisfy(desc->assertThat(desc.toLowerCase()).contains("learn"));
    }
}
