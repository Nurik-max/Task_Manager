package org.example.Task_Manager.Repository.Task;

import org.example.Task_Manager.Model.Status;
import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
public class FindByStatusTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void shouldFindByStatus(){

        Task task1 = new Task();
        task1.setStatus(Status.NEW);

        Task task2 = new Task();
        task2.setStatus(Status.DONE);

        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.flush();

        List<Task> taskList = taskRepository.findByStatus(Status.DONE);



        Assertions.assertEquals(1, taskList.size());
        Assertions.assertEquals(Status.DONE, taskList.get(0).getStatus());
        Assertions.assertNotNull(taskList);
    }
}
