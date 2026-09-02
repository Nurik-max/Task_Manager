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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FindTaskByIdTest {

    @Autowired
    private TaskRepository taskRepository;

        @Test
        void findById() {

            Task task = new Task();
            task.setDescription("Hello");
            task.setStatus(Status.DONE);
            task.setPriority(Priority.LOW);

            Task savedTask = taskRepository.save(task);

            Task returnTask = taskRepository.findById(savedTask.getId())
                    .orElse(null);

            Assertions.assertNotNull(returnTask);
            //Assertions.assertEquals(1, task.getId());
            Assertions.assertEquals("Hello",
                    returnTask.getDescription());
        }
    }



