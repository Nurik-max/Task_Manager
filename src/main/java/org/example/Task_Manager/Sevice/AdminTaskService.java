package org.example.Task_Manager.Sevice;

import jakarta.transaction.Transactional;
import org.example.Task_Manager.DTO.tasks.request.AdminCreateTaskRequest;
import org.example.Task_Manager.DTO.tasks.request.AdminUpdateTaskRequest;
import org.example.Task_Manager.DTO.tasks.response.AdminResponse;
import org.example.Task_Manager.Exceptions.TaskNotFoundException;
import org.example.Task_Manager.Exceptions.WorkerNotFoundException;
import org.example.Task_Manager.Model.*;
import org.example.Task_Manager.Repository.TaskMapper;
import org.example.Task_Manager.Repository.TaskRepository;
import org.example.Task_Manager.Repository.WorkerRepository;
import org.example.Task_Manager.details.WorkerDetails;
import org.example.Task_Manager.specification.TaskSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Arrays.stream;

@Service
@Transactional
public class AdminTaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final WorkerRepository workerRepository;

    public AdminTaskService(TaskRepository repository, TaskMapper taskMapper, WorkerRepository workerRepository){

        this.taskRepository = repository;
        this.taskMapper = taskMapper;
        this.workerRepository = workerRepository;
    }

    @Transactional
    public AdminResponse saveTask(AdminCreateTaskRequest taskDTO, WorkerDetails workerDetails) {
        // 1. Превращаем DTO в Entity (подготовка к базе)
        Task task = taskMapper.toEntity(taskDTO);
        Worker currentWorker = workerDetails.getWorker();

        boolean isAdmin = currentWorker.getUserRole() == UserRole.ADMIN;

        // 2. Устанавливаем дату и статус
            task.setCreatedDate(LocalDateTime.now());
            task.setStatus(Status.NEW);


        if (taskDTO.getWorker_id() != null) {

            if (!isAdmin) {
                throw new AccessDeniedException("Only admin can assign tasks");
            }

            Worker worker = workerRepository.findById(taskDTO.getWorker_id())
                    .orElseThrow(() -> new WorkerNotFoundException(taskDTO.getWorker_id()));

            task.setWorker(worker);

        }
        // 3. Сохраняем и получаем объект с уже проставленным ID
        Task savedTask = taskRepository.save(task);
        System.out.println(task.getCreatedDate());
        // 4. Возвращаем DTO! (Контроллер увидит готовый объект с ID)
        return taskMapper.toAdminResponse(savedTask);
    }

    @Transactional
    public AdminResponse updateTask(
            int taskId,
            AdminUpdateTaskRequest taskDTO,
            WorkerDetails workerDetails
    ) {

        Worker currentWorker = workerDetails.getWorker();
        Task task;
        boolean isAdmin =
                currentWorker.getUserRole() == UserRole.ADMIN;

            task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new TaskNotFoundException(taskId));


        if(task.getUpdatedAt() == null){
            task.setUpdatedAt(LocalDateTime.now());
        }

        //Rewrite all empty variable with old data
     taskMapper.updateTaskForAdmin(task, taskDTO);

        if (taskDTO.getUpdatedWorkerId() != null) {

            if (!isAdmin) {
                throw new AccessDeniedException("Only admin can reassign");
            }

            Worker newWorker = workerRepository.findById(taskDTO.getUpdatedWorkerId())
                    .orElseThrow(() -> new WorkerNotFoundException(taskDTO.getUpdatedWorkerId()));

            task.setWorker(newWorker);
        }

        return taskMapper.toAdminResponse(task);
    }

    public AdminResponse showTask(int id, WorkerDetails workerDetails){

        return taskMapper.toAdminResponse(getAccessibleTask(id, workerDetails));
    }

    @Transactional
    public void softDeleteTask(int id, WorkerDetails workerDetails) {
      Task task = getAccessibleTask(id, workerDetails);
        task.setDeleted(true);
        task.setDeletedAt(LocalDateTime.now());
//        taskRepository.save(task);
//        System.out.println(task.getDeletedAt());
    }

    @Transactional
    public  void hardDeleteTask(int id, WorkerDetails workerDetails){
     Task task = getAccessibleTask(id, workerDetails);
        taskRepository.delete(task);
    }

    @Transactional
    public void restoreTask(int id, WorkerDetails workerDetails) {
        Task task = getAccessibleTask(id, workerDetails);
        task.setDeleted(false); // Восстанавливаем
        task.setDeletedAt(null);
        //taskRepository.save(task);
    }


    //for Admin, he can get all tasks
    public Page<AdminResponse> getTasks(Status status, Priority priority, String keyword, String username,
                               LocalDateTime start, LocalDateTime end, boolean isDeleted ,Pageable pageable){

        Specification<Task> spec = Specification.where(TaskSpecifications.isDeleted(isDeleted));

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

        if(username != null){
            spec = spec.and(TaskSpecifications.hasWorker(username));
        }

//        // Временно замени всё в getTasks на это:
//        spec = (root, query, cb) -> cb.equal(root.get("status"), Status.NEW);

        System.out.println(spec);
        Page<Task> taskPage = taskRepository.findAll(spec, pageable);
        return taskPage.map(taskMapper::toAdminResponse);
    }

//    //for Users, can get only his own tasks
// @Transactional
// public Page<TaskDTO> workerListOfTask(
//         WorkerDetails workerDetails,
//         Status status,
//         Priority priority,
//         String keyword,
//         LocalDateTime start, LocalDateTime end,
//         Pageable pageable,
//         boolean isDeleted) {
//
//     Worker currentWorker = workerDetails.getWorker();
//
//     Specification<Task> spec =
//             Specification.where(TaskSpecifications.isDeleted(isDeleted));
//     //filter for status
//     if(status != null){
//         spec = spec.and(TaskSpecifications.hasStatus(status));
//     }
//
//     //filter priority
//     if(priority != null){
//         spec = spec.and(TaskSpecifications.hasPriority(priority));
//     }
//
//     //filter for Description
//     if(keyword != null && !keyword.isBlank()){
//         spec = spec.and(TaskSpecifications.hasKeyword(keyword));
//     }
//
//
//     //filter by date
//     if(start != null){
//         spec = spec.and(TaskSpecifications.createdAfter(start));
//     }
//
//     if(end != null){
//         spec = spec.and(TaskSpecifications.createdBefore(end));
//     }
//
//
//     if (currentWorker.getUserRole() != UserRole.ADMIN) {
//         spec = spec.and(
//                 TaskSpecifications.hasWorker(
//                         currentWorker.getUsername()
//                 )
//         );
//     }
//
//     return taskRepository.findAll(spec, pageable)
//             .map(taskMapper::toDTO);
// }
 @Transactional
 @PreAuthorize("hasRole('ADMIN')")
    public void taskReassignment(int oldWorkerId, int newWorkerId){

       if(!workerRepository.existsWorkerById(oldWorkerId)){
           throw new WorkerNotFoundException(oldWorkerId);
     } else if (oldWorkerId == newWorkerId) {
           return;
       }

       List<Task> taskList = taskRepository.findByWorker_Id(oldWorkerId);
        Worker newWorker = workerRepository.findById(newWorkerId).orElseThrow(()-> new WorkerNotFoundException(newWorkerId));

        for(Task task: taskList){
            task.setWorker(newWorker);
        }

    }
    @Transactional
    public List<Task> getTasksByWorkerId(int updatedWorker){

        return taskRepository.findByWorker_Id(updatedWorker);
    }

    private Task getAccessibleTask(
            int taskId,
            WorkerDetails workerDetails) {
        Worker currentWorker = workerDetails.getWorker();
        Task task;
        if (currentWorker.getUserRole() == UserRole.ADMIN) {

           task = taskRepository.findById(taskId)            // ...и никак его не использовали!
                    .orElseThrow(() -> new TaskNotFoundException(taskId));
        }
        else {
            task = taskRepository.findByIdAndWorkerUsername(taskId,workerDetails.getUsername()).
                    orElseThrow(() -> new TaskNotFoundException(taskId));

        }
 return  task;
    }


    public Page<AdminResponse> getDeletedTasks(
            WorkerDetails workerDetails,
            Pageable pageable) {

        if (workerDetails.getWorker().getUserRole() == UserRole.ADMIN) {
            return taskRepository.findByIsDeletedTrue(pageable)
                    .map(taskMapper::toAdminResponse);
        }

        return taskRepository
                .findByWorkerIdAndIsDeletedTrue(workerDetails.getWorker().getId(), pageable)
                .map(taskMapper::toAdminResponse);
    }


}
