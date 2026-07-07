package org.example.Task_Manager.Sevice;

import jakarta.transaction.Transactional;
import org.example.Task_Manager.DTO.workers.AdminCreateWorkerDTO;
import org.example.Task_Manager.DTO.workers.ChangePasswordDTO;
import org.example.Task_Manager.DTO.workers.CreateWorkerDTO;
import org.example.Task_Manager.DTO.workers.response.AdminWorkerResponse;
import org.example.Task_Manager.DTO.workers.response.WorkerDTO;
import org.example.Task_Manager.DTO.workers.UpdateWorkerDTO;
import org.example.Task_Manager.DTO.workers.request.ProfileUpdateDTO;
import org.example.Task_Manager.Exceptions.ValidationException;
import org.example.Task_Manager.Exceptions.WorkerNotFoundException;
import org.example.Task_Manager.Model.*;
import org.example.Task_Manager.Repository.WorkerMapper;
import org.example.Task_Manager.Repository.WorkerRepository;
import org.example.Task_Manager.details.WorkerDetails;
import org.example.Task_Manager.specification.WorkerSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private final AdminTaskService adminTaskService;

    private static final Logger log = LoggerFactory.getLogger(WorkerService.class);




    public WorkerService(WorkerMapper workerMapper, BCryptPasswordEncoder passwordEncoder, WorkerRepository workerRepository, AdminTaskService adminTaskService) {
        this.workerMapper = workerMapper;
        this.passwordEncoder = passwordEncoder;
        this.workerRepository = workerRepository;
        this.adminTaskService = adminTaskService;
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
    public AdminWorkerResponse createWorker(AdminCreateWorkerDTO adminCreateWorkerDTO) {
        Worker worker = workerMapper.adminCreateWorkerFromDTO(adminCreateWorkerDTO);
         worker.setPassword(passwordEncoder.encode(adminCreateWorkerDTO.getPassword()));
         worker.setCreatedDate(LocalDateTime.now());
       Worker savedWorker =  workerRepository.save(worker);

    System.out.println(adminCreateWorkerDTO.getEmail());
    System.out.println(adminCreateWorkerDTO.getCreatedAt());
       return workerMapper.toAdminWorkerResponse(savedWorker);
    }


@Transactional
    public AdminWorkerResponse updateWorker(int id, UpdateWorkerDTO updateWorkerDTO){

        Worker existingWorker = workerRepository.findById(id).orElseThrow(() -> new WorkerNotFoundException(id));

        workerMapper.updateWorkerFromDTO(updateWorkerDTO, existingWorker);

        Worker updatedWorker = workerRepository.save(existingWorker);
        return workerMapper.toAdminWorkerResponse(updatedWorker);
    }

    @Transactional
    public WorkerDTO updateProfile(int id, ProfileUpdateDTO profileUpdateDTO) {

        Worker existingWorker = workerRepository.findById(id).orElseThrow(() -> new WorkerNotFoundException(id));
        workerMapper.updateProfileFromDTO(profileUpdateDTO, existingWorker);
        Worker updatedWorker = workerRepository.save(existingWorker);
        return workerMapper.toUserResponse(updatedWorker);
    }

    public WorkerDTO showWorker(int id){
       Worker worker = workerRepository.findById(id).orElseThrow(() -> new WorkerNotFoundException(id));
//       List<Task> workerListOfTask = taskService.workerListOfTask(id);
       return workerMapper.toUserResponse(worker);
    }

    @Transactional
    public AdminWorkerResponse softDeleteWorker(int id){
        Worker worker = workerRepository.findById(id).orElseThrow(() -> new WorkerNotFoundException(id));
        worker.setWorkerStatus(WorkerStatus.FIRED);
        return workerMapper.toAdminWorkerResponse(worker);
    }

    @Transactional
    public AdminWorkerResponse hardDeleteWorker(int id, Integer newWorkerId) {
        // 1. Проверяем: если новый ID передан, он не должен совпадать с удаляемым
        if (newWorkerId != null && id == newWorkerId) {
            throw new IllegalArgumentException("Нельзя переназначить задачи самому себе!");
        }

        // 2. Находим работника
        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new WorkerNotFoundException(id));

        // 3. Выполняем переназначение ТОЛЬКО если указан новый работник
        if (newWorkerId != null) {
            adminTaskService.taskReassignment(id, newWorkerId);
        }

        // 4. Удаляем работника
        workerRepository.delete(worker);
        log.info("Worker deleted successfully: {}", id);
        return workerMapper.toAdminWorkerResponse(worker);
    }


    public Page<AdminWorkerResponse> getWorkers(String username, String surname,String position, WorkerStatus workerStatus, Pageable pageable) {

        // 1. Начинаем с базового условия (исключаем уволенных)
        Specification<Worker> spec = Specification.where(WorkerSpecification.isNotFired());

        // 2. Динамически добавляем фильтры, если они переданы
        if (username != null && !username.isBlank()) {
            spec = spec.and(WorkerSpecification.hasName(username));
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
        return workerPage.map(workerMapper::toAdminWorkerResponse);
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
    public AdminWorkerResponse restoreWorker(int id) {
        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new WorkerNotFoundException(id));

        worker.setWorkerStatus(WorkerStatus.WORKS); // Возвращаем статус

        return workerMapper.toAdminWorkerResponse(worker);
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
