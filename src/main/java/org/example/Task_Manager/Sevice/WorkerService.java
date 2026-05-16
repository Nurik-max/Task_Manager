package org.example.Task_Manager.Sevice;

import jakarta.transaction.Transactional;
import org.example.Task_Manager.DTO.WorkerDTO;
import org.example.Task_Manager.Exceptions.WorkerNotFoundException;
import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Model.WorkerStatus;
import org.example.Task_Manager.Repoitory.WorkerMapper;
import org.example.Task_Manager.Repoitory.WorkerRepository;
import org.example.Task_Manager.specification.WorkerSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class WorkerService {

    private final WorkerMapper workerMapper;

    private final WorkerRepository workerRepository;
    private final TaskService taskService;


    public WorkerService(WorkerRepository workerRepository, WorkerMapper workerMapper, TaskService taskService){
        this.workerMapper = workerMapper;
        this.workerRepository = workerRepository;
        this.taskService = taskService;
    }
@Transactional
    public void saveWorker(WorkerDTO workerDTO) {
        Worker worker = workerMapper.toEntity(workerDTO);

        // Если статус не задан или null, ставим дефолтный
        if (worker.getWorkerStatus() == null) {
            worker.setWorkerStatus(WorkerStatus.WORKS);
        }

        workerRepository.save(worker);
    }
@Transactional
    public WorkerDTO updateWorker(int id, WorkerDTO workerDTO){

        Worker existingWorker = workerRepository.findById(id).orElseThrow(() -> new WorkerNotFoundException(id));

        existingWorker.setName(workerDTO.getName());
        existingWorker.setSurname(workerDTO.getSurname());
        existingWorker.setPosition(workerDTO.getPosition());

        Worker savedWorker = workerRepository.save(existingWorker);
        return workerMapper.toDTO(savedWorker);
    }

    public WorkerDTO showWorker(int id){
       Worker worker = workerRepository.findById(id).orElseThrow(() -> new WorkerNotFoundException(id));
       List<Task> workerListOfTask = taskService.workerListOfTask(id);
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
        dto.setName(worker.getName());
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
}
