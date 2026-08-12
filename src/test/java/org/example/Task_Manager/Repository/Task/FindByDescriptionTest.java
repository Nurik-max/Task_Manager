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
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FindByDescriptionTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void shouldReturnDescription(){

    Task task1 = new Task();
        task1.setDescription("Learn Integration test");
        task1.setStatus(Status.NEW);
        task1.setPriority(Priority.HIGH);

        Task task2 = new Task();
        task2.setDescription("Write One connect list");
        task2.setStatus(Status.NEW);
        task2.setPriority(Priority.HIGH);

        Task task3 = new Task();
        task3.setDescription("learn data structure");
        task3.setStatus(Status.NEW);
        task3.setPriority(Priority.HIGH);

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
