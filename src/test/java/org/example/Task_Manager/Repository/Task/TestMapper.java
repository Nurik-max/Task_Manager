package org.example.Task_Manager.Repository.Task;

import org.example.Task_Manager.DTO.tasks.TaskDTO;
import org.example.Task_Manager.DTO.tasks.request.AdminCreateTaskRequest;
import org.example.Task_Manager.DTO.tasks.request.AdminUpdateTaskRequest;
import org.example.Task_Manager.DTO.tasks.request.CreateTaskRequest;
import org.example.Task_Manager.DTO.tasks.request.UpdateTaskRequest;
import org.example.Task_Manager.DTO.tasks.response.AdminResponse;
import org.example.Task_Manager.DTO.tasks.response.UserResponse;
import org.example.Task_Manager.Model.Priority;
import org.example.Task_Manager.Model.Status;
import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Repository.TaskMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestMapper {
    @Autowired
    private TaskMapper taskMapper;



    @Test
    void toUserResponseShouldMapWorkerUsername() {
        Worker worker = new Worker();
        worker.setUsername("William");

        Task task = new Task();
        task.setWorker(worker);
        task.setDescription("Test task");

        UserResponse response = taskMapper.toUserResponse(task);

        assertEquals("William", response.getWorkerUsername());
        assertEquals("Test task", response.getDescription());
    }

    @Test
    void toAdminResponseShouldMapWorkerData() {
        Worker worker = new Worker();
        worker.setId(10);
        worker.setUsername("William");
        worker.setSurname("Smith");

        Task task = new Task();
        task.setWorker(worker);
        task.setDescription("Test task");

        AdminResponse response = taskMapper.toAdminResponse(task);

        assertEquals(10, response.getWorkerId());
        assertEquals("William", response.getUsername());
        assertEquals("Smith", response.getSurname());
        assertEquals("Test task", response.getDescription());
    }

    @Test
    void toEntityShouldIgnoreWorkerForUserRequest() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setDescription("Test task");

        Task task = taskMapper.toEntity(request);

        assertEquals("Test task", task.getDescription());
        assertNull(task.getWorker());
    }

    @Test
    void toEntityShouldIgnoreWorkerForAdminRequest() {
        AdminCreateTaskRequest request = new AdminCreateTaskRequest();
        request.setDescription("Admin task");

        Task task = taskMapper.toEntity(request);

        assertEquals("Admin task", task.getDescription());
        assertNull(task.getWorker());
    }

    @Test
    void updateTaskForAdminShouldUpdateNonNullFieldsAndIgnoreNulls() {
        Task task = new Task();
        task.setDescription("Old description");
        task.setStatus(Status.NEW);

        AdminUpdateTaskRequest request = new AdminUpdateTaskRequest();
        request.setDescription("New description");
        request.setStatus(null);

        taskMapper.updateTaskForAdmin(task, request);

        assertEquals("New description", task.getDescription());
        assertEquals(Status.NEW, task.getStatus());
    }

    @Test
    void updateTaskForUserShouldUpdateNonNullFieldsAndIgnoreNulls() {
        Task task = new Task();
        task.setDescription("Old description");
        task.setPriority(Priority.HIGH);

        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setDescription("New description");
        request.setPriority(null);

        taskMapper.updateTaskForUser(task, request);

        assertEquals("New description", task.getDescription());
        assertEquals(Priority.HIGH, task.getPriority());
    }
}
