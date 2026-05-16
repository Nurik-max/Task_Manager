package org.example.Task_Manager.Controller;


import jakarta.validation.Valid;
import org.example.Task_Manager.DTO.WorkerDTO;
import org.example.Task_Manager.Exceptions.WorkerNotFoundException;
import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Model.WorkerStatus;
import org.example.Task_Manager.Repoitory.WorkerRepository;
import org.example.Task_Manager.Sevice.TaskService;
import org.example.Task_Manager.Sevice.WorkerService;
import org.example.Task_Manager.specification.WorkerSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final WorkerRepository workerRepository;
    private final TaskService taskService;

    public WorkerController(WorkerService workerService, WorkerRepository workerRepository, TaskService taskService) {
        this.workerService = workerService;
        this.workerRepository = workerRepository;
        this.taskService = taskService;
    }

    @GetMapping
    public String listWorkers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String name,
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
        Page<WorkerDTO> workersPage = workerService.getWorkers(name, surname, position, statusEnum, pageable);

        // 4. Добавляем данные в модель для отображения
        model.addAttribute("workers", workersPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", workersPage.getTotalPages());

        // 5. Прокидываем фильтры обратно, чтобы в input-полях формы оставались введенные значения
        model.addAttribute("name", name);
        model.addAttribute("surname", surname);
        model.addAttribute("position", position);
        model.addAttribute("selectedStatus", status);

        return "workers/list"; // Путь к твоему HTML-файлу со списком
    }

    @GetMapping("/new")
    public String newWorker(Model model) {
        Worker worker = new Worker();
        model.addAttribute("worker", new WorkerDTO());
        System.out.println("Получен воркер: " + worker.getName() + " " + worker.getSurname());

        return "workers/new";
    }

    @PostMapping
    public String create(@ModelAttribute("worker") WorkerDTO workerDTO) {
        workerService.saveWorker(workerDTO);
        return "redirect:/workers";
    }

    // 2. Метод для перехода к редактированию (localhost:8080/workers/1/edit)
    @GetMapping("/{id}/edit")
    public String showOrEditWorker(@PathVariable("id") int id, Model model) {
       addWorkerDependenciesToModel(id, model);
        return "workers/edit"; // Открывает файл edit.html
    }

    @PatchMapping("/{id}")
   public String updateWorker(@ModelAttribute("worker")  @Valid WorkerDTO workerDTO,
                              BindingResult bindingResult, @PathVariable("id") int id){

        if(bindingResult.hasErrors()){
            return "workers/edit";
        }

        workerService.updateWorker(id, workerDTO);
        return "redirect:/workers";
   }
    @GetMapping("/trash")
    public String showTrash(Model model, Pageable pageable) {
        // Используем нашу новую спецификацию
        Page<Worker> firedWorkers = workerRepository.findAll(WorkerSpecification.isFired(), pageable);
        model.addAttribute("workers", firedWorkers);
        return "workers/trash"; // Путь к твоему новому HTML-файлу
    }

    @PostMapping("/{id}")
    public String softDeleteWorker(@PathVariable("id") int id){
        Worker worker = workerRepository.findById(id).orElseThrow(()-> new WorkerNotFoundException(id));
        worker.setWorkerStatus(WorkerStatus.FIRED);
        workerService.softDeleteWorker(id);
        workerRepository.save(worker);
        return "redirect:/workers";
    }
    // Измени @GetMapping на @DeleteMapping
// Измени путь на "/{id}/delete"
    @PostMapping("/{id}/force-delete")
    public String hardDeleteWorker(@PathVariable("id") int id, @RequestParam(required = false) Integer newWorkerId) {

        System.out.println(">>> Запрос на удаление получен! ID = " + id + ", NewWorkerId = " + newWorkerId);
        List<Task> tasks = taskService.workerListOfTask(id);
        if (!tasks.isEmpty() && newWorkerId == null) {
            return "redirect:/workers/" + id + "/edit?error=has_tasks";
        }
        workerService.hardDeleteWorker(id, newWorkerId);
        return "redirect:/workers/trash"; // Или куда тебе нужно после удаления
    }

    @PostMapping("/{id}/restore")
    public String restoreWorker(@PathVariable int id) {
        workerService.restoreWorker(id);
        return "redirect:/workers"; // Возвращаемся в корзину
    }

    // 1. Создай этот вспомогательный метод внутри контроллера, чтобы не дублировать код
    private void addWorkerDependenciesToModel(int id, Model model) {
        model.addAttribute("worker", workerService.showWorker(id));

        // Получаем список задач
        List<Task> tasks = taskService.workerListOfTask(id);
        model.addAttribute("tasks", tasks);

        // Получаем список всех работников, кроме того, которого редактируем
        List<Worker> allWorkers = workerRepository.findAll();
        allWorkers.removeIf(w -> w.getId() == id);
        model.addAttribute("allWorkers", allWorkers);
    }
}
