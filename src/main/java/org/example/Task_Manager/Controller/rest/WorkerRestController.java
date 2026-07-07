package org.example.Task_Manager.Controller.rest;

import lombok.RequiredArgsConstructor;
import org.example.Task_Manager.DTO.workers.AdminCreateWorkerDTO;
import org.example.Task_Manager.DTO.workers.UpdateWorkerDTO;
import org.example.Task_Manager.DTO.workers.response.AdminWorkerResponse;
import org.example.Task_Manager.DTO.workers.response.WorkerDTO;

import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Model.WorkerStatus;
import org.example.Task_Manager.Repository.WorkerMapper;
import org.example.Task_Manager.Repository.WorkerRepository;
import org.example.Task_Manager.Sevice.AdminTaskService;
import org.example.Task_Manager.Sevice.WorkerService;
import org.example.Task_Manager.specification.WorkerSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workers")
@RequiredArgsConstructor
public class WorkerRestController {

    private final WorkerService workerService;
    private final WorkerRepository workerRepository;
    private final WorkerMapper workerMapper;
    private final AdminTaskService adminTaskService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AdminWorkerResponse>> listWorkers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String  username,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String status) {

        // 1. Создаем объект Pageable (по 10 записей на страницу)
        Pageable pageable = PageRequest.of(page, 10);

        // 2. Безопасно конвертируем String status в Enum WorkerStatus
        WorkerStatus statusEnum = null;
        if (status != null && !status.isBlank()) {
            try {
                statusEnum = WorkerStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Если ввели несуществующий статус, игнорируем фильтр
            }
        }

        // 3. Вызываем сервис (метод должен возвращать Page<WorkerDTO>)
        Page<AdminWorkerResponse> workersPage = workerService.getWorkers(username, surname, position, statusEnum, pageable);


        return ResponseEntity.ok(workersPage); // Путь к твоему HTML-файлу со списком
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminWorkerResponse> create(@RequestBody AdminCreateWorkerDTO adminCreateWorkerDTO) {
      AdminWorkerResponse savedWorker =  workerService.createWorker(adminCreateWorkerDTO);
        System.out.println(savedWorker.getCreatedAt());
        System.out.println(savedWorker.getEmail());
        System.out.println(savedWorker.getId());
        return ResponseEntity.ok(savedWorker);
    }


    @PutMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.worker.id or hasRole('ADMIN')")
    public ResponseEntity<AdminWorkerResponse> update(@PathVariable("id") int id, @RequestBody UpdateWorkerDTO workerUpdateDTO) {
      AdminWorkerResponse updatedWorker = workerService.updateWorker(id, workerUpdateDTO);
        return  ResponseEntity.ok(updatedWorker);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/trash")
    public ResponseEntity<Page<AdminWorkerResponse>> showTrash( Pageable pageable) {
        // Используем нашу новую спецификацию
        Page<Worker> firedWorkers = workerRepository.findAll(WorkerSpecification.isFired(), pageable);
        Page<AdminWorkerResponse> workers = firedWorkers.map(workerMapper::toAdminWorkerResponse);

        return  ResponseEntity.ok(workers);
    }

    @PostMapping("/{id}/soft-delete")
    @PreAuthorize("#id == authentication.principal.worker.id or hasRole('ADMIN')")
    public ResponseEntity<AdminWorkerResponse> softDeleteWorker(@PathVariable("id") int id){

       AdminWorkerResponse response = workerService.softDeleteWorker(id);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}hard-delete")
    public ResponseEntity<Void> hardDeleteWorker(
            @PathVariable int id,
            @RequestParam(required = false) Integer newWorkerId) {

        workerService.hardDeleteWorker(id, newWorkerId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    @PreAuthorize("#id == authentication.principal.worker.id or hasRole('ADMIN')")
    public ResponseEntity<AdminWorkerResponse> restoreWorker(@PathVariable int id) {
        AdminWorkerResponse restoredWorker = workerService.restoreWorker(id);
        return ResponseEntity.ok(restoredWorker);
    }

}
