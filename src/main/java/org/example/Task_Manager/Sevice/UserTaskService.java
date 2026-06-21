package org.example.Task_Manager.Sevice;

import jakarta.transaction.Transactional;
import org.example.Task_Manager.DTO.TaskDTO;
import org.example.Task_Manager.DTO.tasks.CreateTaskRequest;
import org.example.Task_Manager.DTO.tasks.UpdateTaskRequest;
import org.example.Task_Manager.Exceptions.TaskNotFoundException;
import org.example.Task_Manager.Model.*;
import org.example.Task_Manager.Repoitory.TaskMapper;
import org.example.Task_Manager.Repoitory.TaskRepository;
import org.example.Task_Manager.details.WorkerDetails;
import org.example.Task_Manager.specification.TaskSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
@Transactional
public class UserTaskService {


    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public UserTaskService(TaskRepository repository, TaskMapper taskMapper)
    {
        this.taskRepository = repository;
        this.taskMapper = taskMapper;
    }

    //for Users, can get only his own tasks
    @Transactional
    public Page<TaskDTO> workerListOfTask(
            WorkerDetails workerDetails,
            Status status,
            Priority priority,
            String keyword,
            LocalDateTime start, LocalDateTime end,
            Pageable pageable,
            boolean isDeleted) {

        Worker currentWorker = workerDetails.getWorker();

        Specification<Task> spec =
                Specification.where(TaskSpecifications.isDeleted(isDeleted));
        //filter for status
        if(status != null){
            spec = spec.and(TaskSpecifications.hasStatus(status));
        }

        //filter priority
        if(priority != null){
            spec = spec.and(TaskSpecifications.hasPriority(priority));
        }

        //filter for Description
        if(keyword != null && !keyword.isBlank()){
            spec = spec.and(TaskSpecifications.hasKeyword(keyword));
        }


        //filter by date
        if(start != null){
            spec = spec.and(TaskSpecifications.createdAfter(start));
        }

        if(end != null){
            spec = spec.and(TaskSpecifications.createdBefore(end));
        }


        if (currentWorker.getUserRole() != UserRole.ADMIN) {
            spec = spec.and(
                    TaskSpecifications.hasWorker(
                            currentWorker.getUsername()
                    )
            );
        }

        return taskRepository.findAll(spec, pageable)
                .map(taskMapper::toDTO);
    }

    public TaskDTO saveUserTask(CreateTaskRequest request, WorkerDetails workerDetails)
    {
        // 1. Превращаем DTO в Entity (подготовка к базе)
        Task task = taskMapper.toEntity(request);
        Worker currentWorker = workerDetails.getWorker();

        // 2. Устанавливаем дату и статус
        task.setCreatedDate(LocalDateTime.now());
        task.setStatus(Status.NEW);

        if(currentWorker != null)
        {
            task.setWorker(currentWorker);
        }
        taskRepository.save(task);
        return taskMapper.toDTO(task);
    }

    public TaskDTO updateUserTask(int taskId, UpdateTaskRequest taskDTO, WorkerDetails workerDetails)
    {

        Worker currentWorker = workerDetails.getWorker();
        Task task = taskRepository.findByIdAndWorkerUsername(
                taskId,
                workerDetails.getUsername()
        ).orElseThrow(() -> new TaskNotFoundException(taskId));

        if(task.getUpdatedAt() == null){
            task.setUpdatedAt(LocalDateTime.now());
        }

        //Rewrite all empty variable with old data
        taskMapper.updateTaskForUser(task, taskDTO);

        return taskMapper.toDTO(taskRepository.save(task));
    }


}
