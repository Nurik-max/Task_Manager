package org.example.Task_Manager.Repository.Task;

import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Repoitory.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
public class findByIsDeletedTrueTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void shouldReturnDeletedTask(){

        Task task1 = new Task();
        task1.setDeleted(false);

        Task task2 = new Task();
        task2.setDeleted(true);

        Task task3 = new Task();
        task3.setDeleted(true);

        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);

        Pageable pageable = PageRequest.of(0,2);

        Page<Task> result = taskRepository.findByIsDeletedTrue(pageable);

//        for(int i = 0; i < 2; i++){
//            Assertions.assertTrue(result.getContent().get(result.getNumber()).isDeleted());
//        }

        result.getContent().forEach(task -> Assertions.assertTrue(task.isDeleted()));

        Assertions.assertEquals(2, result.getContent().size());
        Assertions.assertEquals(2, result.getTotalElements());
        Assertions.assertEquals(1, result.getTotalPages());
    }
}
