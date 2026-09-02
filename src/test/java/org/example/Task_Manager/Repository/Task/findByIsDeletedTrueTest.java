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
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class findByIsDeletedTrueTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void shouldReturnDeletedTask(){

        Task task1 = new Task();
        task1.setDeleted(false);
        task1.setDescription("learn data structure");
        task1.setPriority(Priority.HIGH);
        task1.setStatus(Status.DONE);

        Task task2 = new Task();
        task2.setDeleted(true);
        task2.setDescription("learn Spring");
        task2.setPriority(Priority.HIGH);
        task2.setStatus(Status.DONE);

        Task task3 = new Task();
        task3.setDeleted(true);
        task3.setDescription("learn docker");
        task3.setPriority(Priority.HIGH);
        task3.setStatus(Status.DONE);

        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);

        Pageable pageable = PageRequest.of(0,2);

        Page<Task> result = taskRepository.findByIsDeletedTrue(pageable);

        for(int i = 0; i < 2; i++){
            Assertions.assertTrue(result.getContent().get(result.getNumber()).isDeleted());
        }
        result.getContent().forEach(task -> Assertions.assertTrue(task.isDeleted()));

        Assertions.assertEquals(2, result.getContent().size());
        Assertions.assertEquals(2, result.getTotalElements());
        Assertions.assertEquals(1, result.getTotalPages());
    }
}
