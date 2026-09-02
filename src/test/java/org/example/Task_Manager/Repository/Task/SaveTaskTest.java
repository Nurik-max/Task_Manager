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
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;


import static org.mockito.Mockito.verify;


@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SaveTaskTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void saveTask(){

        Task task = new Task();
        task.setDescription("Never give up");
        task.setStatus(Status.NEW);
        task.setPriority(Priority.LOW);

       Task savedTask = taskRepository.save(task);

        Assertions.assertNotNull(savedTask);
        Assertions.assertNotNull(task.getId());
        System.out.println(task.getId());
    }

    @Test
    void shouldThrowExceptionWhenTaskIsNull() {

        InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
                InvalidDataAccessApiUsageException.class,
                () -> taskRepository.save(null)
        );

        Assertions.assertTrue(exception.getMessage().contains("must not be null")
                || exception.getCause() instanceof IllegalArgumentException);
    }
}
