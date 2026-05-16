package org.example.Task_Manager.Sevice;

import jakarta.transaction.Transactional;
import org.example.Task_Manager.DTO.workers.AdminCreateWorkerDTO;
import org.example.Task_Manager.DTO.workers.ChangePasswordDTO;
import org.example.Task_Manager.DTO.CreateWorkerDTO;
import org.example.Task_Manager.DTO.WorkerDTO;
import org.example.Task_Manager.DTO.workers.UpdateWorkerDTO;
import org.example.Task_Manager.Exceptions.ValidationException;
import org.example.Task_Manager.Exceptions.WorkerNotFoundException;
import org.example.Task_Manager.Model.*;
import org.example.Task_Manager.Repoitory.WorkerMapper;
import org.example.Task_Manager.Repoitory.WorkerRepository;
import org.example.Task_Manager.details.WorkerDetails;
import org.example.Task_Manager.specification.WorkerSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class WorkerService {

    @Autowired
    private final WorkerMapper workerMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private final WorkerRepository workerRepository;

    @Autowired
    private final TaskService taskService;


    public WorkerService(WorkerMapper workerMapper, BCryptPasswordEncoder passwordEncoder, WorkerRepository workerRepository, TaskService taskService) {
        this.workerMapper = workerMapper;
        this.passwordEncoder = passwordEncoder;
        this.workerRepository = workerRepository;
        this.taskService = taskService;
    }

    //Method for users
    public void register(CreateWorkerDTO dto){

        if(!dto.getPassword().equals(dto.getConfirmPassword())){
            throw new ValidationException("Passwords do not match");
        }

        if (workerRepository.existsWorkerByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
       Worker worker = workerMapper.toEntity(dto);
//        worker.setUserRole(UserRole.USER);
//        worker.setWorkerStatus(WorkerStatus.WORKS);
        worker.setPassword(passwordEncoder.encode(dto.getPassword()));
        workerRepository.save(worker);

    }

@Transactional //Method for admin
    public void createWorker(AdminCreateWorkerDTO adminCreateWorkerDTO) {
        Worker worker = workerMapper.adminCreateWorkerFromDTO(adminCreateWorkerDTO);

    worker.setPassword(passwordEncoder.encode(adminCreateWorkerDTO.getPassword()));
        workerRepository.save(worker);
    }


@Transactional
    public Worker updateWorker(int id, UpdateWorkerDTO updateWorkerDTO){

        Worker existingWorker = workerRepository.findById(id).orElseThrow(() -> new WorkerNotFoundException(id));

        workerMapper.updateWorkerFromDTO(updateWorkerDTO, existingWorker);
        return existingWorker;
    }

    public WorkerDTO showWorker(int id){
       Worker worker = workerRepository.findById(id).orElseThrow(() -> new WorkerNotFoundException(id));
//       List<Task> workerListOfTask = taskService.workerListOfTask(id);
       return workerMapper.toDTO(worker);
    }

    @Transactional
    public void softDeleteWorker(int id){

        Worker worker = workerRepository.findById(id).orElseThrow(() -> new WorkerNotFoundException(id));
        worker.setWorkerStatus(WorkerStatus.FIRED);
    }

    @Transactional
    public void hardDeleteWorker(int id, Integer newWorkerId) {
        // 1. Проверяем: если новый ID передан, он не должен совпадать с удаляемым
        if (newWorkerId != null && id == newWorkerId) {
            throw new IllegalArgumentException("Нельзя переназначить задачи самому себе!");
        }

        // 2. Находим работника
        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new WorkerNotFoundException(id));

        // 3. Выполняем переназначение ТОЛЬКО если указан новый работник
        if (newWorkerId != null) {
            taskService.taskReassignment(id, newWorkerId);
        }

        // 4. Удаляем работника
        workerRepository.delete(worker);
        System.out.println(">>> Удаляю worker: " + id);
    }


    public Page<WorkerDTO> getWorkers(String name, String surname, String position, WorkerStatus workerStatus, Pageable pageable) {

        // 1. Начинаем с базового условия (исключаем уволенных)
        Specification<Worker> spec = Specification.where(WorkerSpecification.isNotFired());

        // 2. Динамически добавляем фильтры, если они переданы
        if (name != null && !name.isBlank()) {
            spec = spec.and(WorkerSpecification.hasName(name));
        }
        if (surname != null && !surname.isBlank()) {
            spec = spec.and(WorkerSpecification.hasSurname(surname));
        }
        if (position != null && !position.isBlank()) {
            spec = spec.and(WorkerSpecification.hasPosition(position));
        }
        if (workerStatus != null) {
            // Если передан конкретный статус (например, ACTIVE), фильтруем по нему
            // (в дополнение к тому, что он не FIRED)
            spec = spec.and(WorkerSpecification.hasStatus(workerStatus));
        }

        // 3. Получаем страницу сущностей
        Page<Worker> workerPage = workerRepository.findAll(spec, pageable);

        // 4. Преобразуем Page<Worker> в Page<WorkerDTO>
        return workerPage.map(this::convertToDTO);
    }

    // Вспомогательный метод для маппинга
    private WorkerDTO convertToDTO(Worker worker) {
        WorkerDTO dto = new WorkerDTO();
        dto.setId(worker.getId());
        dto.setUsername(worker.getUsername());
        dto.setSurname(worker.getSurname());
        dto.setPosition(worker.getPosition());
        dto.setWorkerStatus(worker.getWorkerStatus());
        return dto;
    }
    @Transactional
    public void restoreWorker(int id) {
        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new WorkerNotFoundException(id));

        worker.setWorkerStatus(WorkerStatus.WORKS); // Возвращаем статус
        // save() не обязателен, если есть @Transactional
    }

    @Transactional
    public void changePassword(ChangePasswordDTO dto,
                               WorkerDetails workerDetails) {

        Worker worker = workerDetails.getWorker();

        if (!passwordEncoder.matches(dto.getOldPassword(), worker.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        worker.setPassword(passwordEncoder.encode(dto.getNewPassword()));
    }

    public List<Worker> findAllExcept(int workerId) {
        return workerRepository.findByIdNot(workerId);
    }
}
