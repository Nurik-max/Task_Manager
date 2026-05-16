package org.example.Task_Manager.Sevice;

import jakarta.transaction.Transactional;
import org.example.Task_Manager.DTO.TaskDTO;
import org.example.Task_Manager.Exceptions.TaskNotFoundException;
import org.example.Task_Manager.Exceptions.WorkerNotFoundException;
import org.example.Task_Manager.Model.Status;
import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Repoitory.TaskMapper;
import org.example.Task_Manager.Repoitory.TaskRepository;
import org.example.Task_Manager.Repoitory.WorkerRepository;
import org.example.Task_Manager.specification.TaskSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final WorkerRepository workerRepository;

    public TaskService(TaskRepository repository, TaskMapper taskMapper, WorkerRepository workerRepository){

        this.taskRepository = repository;
        this.taskMapper = taskMapper;
        this.workerRepository = workerRepository;
    }

    @Transactional
    public TaskDTO saveTask(TaskDTO taskDTO) {
        // 1. Превращаем DTO в Entity (подготовка к базе)
        Task task = taskMapper.toEntity(taskDTO);

        // 2. Устанавливаем дату, если её нет
        if (task.getCreatedDate() == null) {
            task.setCreatedDate(LocalDateTime.now());
        }

        if(taskDTO.getWorkerId() != null){
            Worker worker = workerRepository.findById(taskDTO.getWorkerId()).
                    orElseThrow(() -> new WorkerNotFoundException(taskDTO.getWorkerId()));
            task.setWorker(worker);
        }

        // 3. Сохраняем и получаем объект с уже проставленным ID
        Task savedTask = taskRepository.save(task);

        // 4. Возвращаем DTO! (Контроллер увидит готовый объект с ID)
        return taskMapper.toDTO(savedTask);
    }

    @Transactional
    public TaskDTO updateTask(int taskId, TaskDTO taskDTO) {
        // 1. Находим сущность
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        // 2. Обновляем поля только если они не null
        if (taskDTO.getDescription() != null) {
            existingTask.setDescription(taskDTO.getDescription());
        }

        if (taskDTO.getStatus() != null) {
            existingTask.setStatus(taskDTO.getStatus());
        }

        // 3. Обновляем работника, если передан его ID
        if (taskDTO.getWorkerId() != null) {
            Worker worker = workerRepository.findById(taskDTO.getWorkerId())
                    .orElseThrow(() -> new WorkerNotFoundException(taskDTO.getWorkerId()));
            existingTask.setWorker(worker);
        }

        // 4. Сохранение произойдет автоматически при выходе из метода (@Transactional)
        // Мы можем просто вернуть DTO
       // taskRepository.save(existingTask);
        return taskMapper.toDTO(existingTask);
    }

    public TaskDTO showTask(int id){
      Task task = taskRepository.findById(id).
              orElseThrow(() -> new TaskNotFoundException(id));
      return taskMapper.toDTO(task);
    }

    @Transactional
    public void softDeleteTask(int id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        task.setDeleted(true);
        taskRepository.save(task);
    }

    @Transactional
    public  void hardDeleteTask(int id){
        Task task =taskRepository.findById(id).orElseThrow(()-> new TaskNotFoundException(id));
        taskRepository.delete(task);
    }

    @Transactional
    public void restoreTask(int id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        task.setDeleted(false); // Восстанавливаем
        //taskRepository.save(task);
    }


    public Page<TaskDTO> getTasks(Status status, String keyword, String name,
                               LocalDateTime start, LocalDateTime end, boolean isDeleted ,Pageable pageable){

        Specification<Task> spec = Specification.where(TaskSpecifications.isDeleted(isDeleted));

        //filter for status
        if(status != null){
            spec = spec.and(TaskSpecifications.hasStatus(status));
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

        if(name != null){
            spec = spec.and(TaskSpecifications.hasWorker(name));
        }

//        // Временно замени всё в getTasks на это:
//        spec = (root, query, cb) -> cb.equal(root.get("status"), Status.NEW);

        System.out.println(spec);
        Page<Task> taskPage = taskRepository.findAll(spec, pageable);
        return taskPage.map(taskMapper::toDTO);
    }
 @Transactional
    public List<Task> workerListOfTask(int id){

        Worker worker = workerRepository.findById(id).orElseThrow(()-> new WorkerNotFoundException(id));

        return taskRepository.findByWorker(worker);
    }
 @Transactional
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
}
