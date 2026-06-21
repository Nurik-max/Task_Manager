package org.example.Task_Manager.Repository.Task;

import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class FindTaskByIdTest {

    @Autowired
    private TaskRepository taskRepository;

        @Test
        void findById() {

            Task task = new Task();
            task.setDescription("Hello");

            Task savedTask = taskRepository.save(task);

            Task returnTask = taskRepository.findById(savedTask.getId())
                    .orElse(null);

            Assertions.assertNotNull(returnTask);
            //Assertions.assertEquals(1, task.getId());
            Assertions.assertEquals("Hello",
                    returnTask.getDescription());
        }
    }



