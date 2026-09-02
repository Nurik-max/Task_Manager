package org.example.Task_Manager.Repository.Task;

import jakarta.transaction.Transactional;
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

@Transactional
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DeleteTaskTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void deletedTask(){

        Task task = new Task();
        task.setDescription("Good morning");
        task.setPriority(Priority.HIGH);
        task.setStatus(Status.NEW);

        Task saved = taskRepository.save(task);
        int id  = saved.getId();

        taskRepository.delete(saved);
        taskRepository.flush();

        Task deltedTask = taskRepository.findById(id).orElse(null);

        Assertions.assertNull(deltedTask);
    }

        @Test
       void shouldReturnExceptionWhenIdIsNull(){

           InvalidDataAccessApiUsageException exception = Assertions.assertThrows(InvalidDataAccessApiUsageException.class,
                   () -> taskRepository.findById(null)
           );
           Assertions.assertTrue(exception.getMessage().contains("id must not be null")
                   || exception.getCause() instanceof IllegalArgumentException);
       }

    @Test
    void shouldThrowExceptionWhenDeletingNullTask() {

        InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
                InvalidDataAccessApiUsageException.class,
                () -> taskRepository.delete((Task) null)
        );

        Assertions.assertTrue(exception.getMessage().contains("Entity must not be null")
                || exception.getCause() instanceof IllegalArgumentException);
    }
}
