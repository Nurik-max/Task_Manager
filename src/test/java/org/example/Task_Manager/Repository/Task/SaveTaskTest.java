package org.example.Task_Manager.Repository.Task;

import org.example.Task_Manager.Model.Status;
import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Repoitory.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;


import static org.mockito.Mockito.verify;


@DataJpaTest
public class SaveTaskTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void saveTask(){

        Task task = new Task();
        task.setDescription("Never give up");
        task.setStatus(Status.NEW);

       Task savedTask = taskRepository.save(task);

        Assertions.assertNotNull(savedTask);
        Assertions.assertNotNull(task.getId());
        System.out.println(task.getId());
    }

    @Test
    void shouldThrowExceptionWhenTaskIsNull() {

        Assertions.assertThrows(
                InvalidDataAccessApiUsageException.class,
                () -> taskRepository.save(null)
        );
    }
}
