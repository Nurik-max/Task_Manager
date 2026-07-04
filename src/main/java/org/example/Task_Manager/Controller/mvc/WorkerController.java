package org.example.Task_Manager.Controller.mvc;


import jakarta.validation.Valid;
import org.example.Task_Manager.DTO.workers.AdminCreateWorkerDTO;
import org.example.Task_Manager.DTO.workers.ChangePasswordDTO;
import org.example.Task_Manager.DTO.workers.response.AdminWorkerResponse;
import org.example.Task_Manager.DTO.workers.response.WorkerDTO;
import org.example.Task_Manager.DTO.workers.UpdateWorkerDTO;
import org.example.Task_Manager.Exceptions.WorkerNotFoundException;
import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Model.WorkerStatus;
import org.example.Task_Manager.Repository.WorkerMapper;
import org.example.Task_Manager.Repository.WorkerRepository;
import org.example.Task_Manager.Sevice.AdminTaskService;
import org.example.Task_Manager.Sevice.WorkerService;
import org.example.Task_Manager.details.WorkerDetails;
import org.example.Task_Manager.specification.WorkerSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.List;


@Controller
@RequestMapping("/workers")
public class WorkerController {

    private final WorkerService workerService;
    private  final WorkerMapper workerMapper;
    private final WorkerRepository workerRepository;
    private final AdminTaskService adminTaskService;

    public WorkerController(WorkerService workerService, WorkerMapper workerMapper, WorkerRepository workerRepository, AdminTaskService adminTaskService) {
        this.workerService = workerService;
        this.workerMapper = workerMapper;
        this.workerRepository = workerRepository;
        this.adminTaskService = adminTaskService;
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String listWorkers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String  username,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String status, // Принимаем как String
            Model model) {

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
        Page<WorkerDTO> workersPage = workerService.getWorkers(username, surname, position, statusEnum, pageable);

        // 4. Добавляем данные в модель для отображения
        model.addAttribute("workers", workersPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", workersPage.getTotalPages());

        // 5. Прокидываем фильтры обратно, чтобы в input-полях формы оставались введенные значения
        model.addAttribute("name", username);
        model.addAttribute("surname", surname);
        model.addAttribute("position", position);
        model.addAttribute("selectedStatus", status);

        return "workers/list"; // Путь к твоему HTML-файлу со списком
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String newWorker(Model model) {
        model.addAttribute("worker", new AdminCreateWorkerDTO());
        return "workers/new";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String create(@ModelAttribute("worker")AdminCreateWorkerDTO adminCreateWorkerDTO) {
        workerService.createWorker(adminCreateWorkerDTO);
        return "redirect:/workers";
    }

    // 2. Метод для перехода к редактированию (localhost:8080/workers/1/edit)
    @GetMapping("/{id}/edit")
    @PreAuthorize("#id == authentication.principal.worker.id or hasRole('ADMIN')")
    public String showOrEditWorker(@PathVariable("id") int id, Model model) {
       addWorkerDependenciesToModel(id, model);
        return "workers/edit"; // Открывает файл edit.html
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("#id == authentication.principal.worker.id or hasRole('ADMIN')")
    public String update(@PathVariable("id") int id, @ModelAttribute("worker")UpdateWorkerDTO workerUpdateDTO) {
        workerService.updateWorker(id, workerUpdateDTO);
        return "redirect:/workers";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/trash")
    public String showTrash(Model model, Pageable pageable) {
        // Используем нашу новую спецификацию
        Page<Worker> firedWorkers = workerRepository.findAll(WorkerSpecification.isFired(), pageable);
        Page<AdminWorkerResponse> workers = firedWorkers.map(workerMapper::toAdminWorkerResponse);
        model.addAttribute("workers", workers);
        return "workers/trash"; // Путь к твоему новому HTML-файлу
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("#id == authentication.principal.worker.id or hasRole('ADMIN')")
    public String softDeleteWorker(@PathVariable("id") int id){
        Worker worker = workerRepository.findById(id).orElseThrow(()-> new WorkerNotFoundException(id));
        workerService.softDeleteWorker(id);
        return "redirect:/workers";
    }
    // Измени @GetMapping на @DeleteMapping
// Измени путь на "/{id}/delete"
    @PostMapping("/{id}/force-delete")
    @PreAuthorize("#id == authentication.principal.worker.id or hasRole('ADMIN')")
    public String hardDeleteWorker(@PathVariable("id") int id, @RequestParam(required = false) Integer newWorkerId) {

        System.out.println(">>> Запрос на удаление получен! ID = " + id + ", NewWorkerId = " + newWorkerId);
        List<Task> tasks = adminTaskService.getTasksByWorkerId(id);
        if (!tasks.isEmpty() && newWorkerId == null) {
            return "redirect:/workers/" + id + "/edit?error=has_tasks";
        }
        workerService.hardDeleteWorker(id, newWorkerId);
        return "redirect:/workers/trash"; // Или куда тебе нужно после удаления
    }

    @PostMapping("/{id}/restore")
    @PreAuthorize("#id == authentication.principal.worker.id or hasRole('ADMIN')")
    public String restoreWorker(@PathVariable int id) {
        workerService.restoreWorker(id);
        return "redirect:/workers"; // Возвращаемся в корзину
    }

    // 1. Создай этот вспомогательный метод внутри контроллера, чтобы не дублировать код
    private void addWorkerDependenciesToModel(int id, Model model) {
        model.addAttribute("worker", workerService.showWorker(id));

        // Получаем список задач
        List<Task> tasks = adminTaskService.getTasksByWorkerId(id);
        model.addAttribute("tasks", tasks);

        // Получаем список всех работников, кроме того, которого редактируем
        List<Worker> allWorkers = workerService.findAllExcept(id);
        allWorkers.removeIf(w -> w.getId() == id);
        model.addAttribute("allWorkers", allWorkers);
    }
}
