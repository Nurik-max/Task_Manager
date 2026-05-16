package org.example.Task_Manager.Repository.Task;

import jakarta.transaction.Transactional;
import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Repoitory.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@Transactional
@DataJpaTest
public class DeleteTaskTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void deleteTask(){

        Task task = new Task();
        task.setDescription("Good morning");

        Task saved = taskRepository.save(task);
        int id  = saved.getId();

        taskRepository.delete(saved);
        taskRepository.flush();

        Task deltedTask = taskRepository.findById(id).orElse(null);

        Assertions.assertNull(deltedTask);
    }
}
