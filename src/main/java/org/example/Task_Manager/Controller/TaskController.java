package org.example.Task_Manager.Controller;

import jakarta.validation.Valid;
import org.example.Task_Manager.DTO.TaskDTO;
import org.example.Task_Manager.DTO.WorkerDTO;
import org.example.Task_Manager.Model.Priority;
import org.example.Task_Manager.Model.Status;
import org.example.Task_Manager.Model.UserRole;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Repoitory.TaskRepository;
import org.example.Task_Manager.Repoitory.WorkerRepository;
import org.example.Task_Manager.Sevice.TaskService;
import org.example.Task_Manager.details.WorkerDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/tasks")
public class TaskController {


    private final TaskService taskService;

    private final WorkerRepository workerRepository;

    public TaskController( TaskService taskService, WorkerRepository workerRepository) {

        this.taskService = taskService;

        this.workerRepository = workerRepository;
    }

    // 📌 список задач
@PreAuthorize("hasRole('ADMIN')")
    @GetMapping //For Admin
    public String getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(defaultValue = "false") boolean isDeleted,
            Model model) {

      Pageable pageable = PageRequest.of(page,10);

// Конвертируем String status в Enum Status безопасно
      Status statusEnum = null;
      if(status != null && !status.isBlank()){
          try {
              statusEnum = Status.valueOf(status.toUpperCase());
          }catch (IllegalArgumentException e){
// Если в URL ввели ерунду, просто игнорируем фильтр по статусу
              System.out.println("value 'Status' equal 'null' or not announced");
          }
      }
      Priority priorityEnum = null;
      if(priority != null && !priority.isBlank()){
          try{
              priorityEnum = Priority.valueOf(priority.toUpperCase());
          }catch (IllegalArgumentException e){
              System.out.println("value 'Priority' equal 'null' or not announced");
          }
      }
// 🔥 Весь твой сложный if-else заменяется одной строчкой!

      //For usual task
      Page<TaskDTO> tasksDTO = taskService.getTasks(statusEnum,priorityEnum ,keyword,username, start, end, isDeleted, pageable);


        model.addAttribute("tasks", tasksDTO.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", tasksDTO.getTotalPages());
        // Прокидываем фильтры обратно в модель, чтобы сохранить их в полях поиска на странице
        model.addAttribute("selectedStatus", status);
        model.addAttribute("keyword", keyword);
        model.addAttribute("username", username);
        model.addAttribute("createdDate", start);


    model.addAttribute("pageTitle", "All Tasks");
    model.addAttribute("isAdminPage", true);
    model.addAttribute(
            "baseUrl",
            "/tasks"
    );


        return "tasks/tasks";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my")
    public String getAllMyTasks( @AuthenticationPrincipal WorkerDetails workerDetails,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(required = false) String status,
                                 @RequestParam(required = false) String priority,
                                 @RequestParam(required = false) String keyword,
                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
                                 @RequestParam(defaultValue = "false") boolean isDeleted,
                                 Model model){

        Pageable pageable = PageRequest.of(page,10);
        Status statusEnum = null;
        if(status != null && !status.isBlank()){
            try {
                statusEnum = Status.valueOf(status.toUpperCase());
            }catch (IllegalArgumentException e){
// Если в URL ввели ерунду, просто игнорируем фильтр по статусу
            }
        }

        Priority priorityEnum = null;
        if(priority != null && !priority.isBlank()){
            try{
                priorityEnum = Priority.valueOf(priority.toUpperCase());
            }catch (IllegalArgumentException e){

            }
        }

        Page<TaskDTO> taskDTOPage = taskService.workerListOfTask(workerDetails,statusEnum,priorityEnum ,keyword, start, end, pageable, isDeleted);

        model.addAttribute("tasks", taskDTOPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", taskDTOPage.getTotalPages());
        // Прокидываем фильтры обратно в модель, чтобы сохранить их в полях поиска на странице
        model.addAttribute("selectedStatus", status);
        model.addAttribute("keyword", keyword);
        model.addAttribute("createdDate", start);

        model.addAttribute("pageTitle", "My Tasks");
        model.addAttribute("isAdminPage", false);
        model.addAttribute(
                "baseUrl",
                "/tasks/my"
        );

        return "tasks/tasks";

    }

    // 📌 форма создания
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/new")
    public String newTask(Model model, @AuthenticationPrincipal WorkerDetails workerDetails){

        model.addAttribute("task", new TaskDTO());

        if (workerDetails.getWorker().getUserRole() == UserRole.ADMIN) {
            model.addAttribute("workers", workerRepository.findAll());
            model.addAttribute("baseUrl", "/tasks");
        } else {
            model.addAttribute("baseUrl", "/tasks/my");
        }
        return "tasks/new";
    }

    // 📌 создание
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public String createNewTask(@ModelAttribute("task") @Valid TaskDTO taskDTO,
                                BindingResult bindingResult,
                                @AuthenticationPrincipal WorkerDetails workerDetails,
                                Model model) {

        if (bindingResult.hasErrors()) {

            if (workerDetails.getWorker().getUserRole() == UserRole.ADMIN) {
                model.addAttribute("workers", workerRepository.findAll());
                model.addAttribute("baseUrl", "/tasks");
            } else {
                model.addAttribute("baseUrl", "/tasks/my");
            }
            System.out.println("POST HIT");
            System.out.println("ERRORS = " + bindingResult.hasErrors());
            System.out.println("DTO = " + taskDTO);
            return "tasks/new"; // ❗ ВАЖНО: return только тут
        }

        // ✅ СОХРАНЕНИЕ
        taskService.saveTask(taskDTO, workerDetails);


        // ✅ РЕДИРЕКТ ПОСЛЕ УСПЕХА
        return "redirect:/tasks";
    }

    // 📌 показать задачу
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public String showTask(@PathVariable("id") int id, @AuthenticationPrincipal WorkerDetails workerDetails ,Model model){
        model.addAttribute("task", taskService.showTask(id, workerDetails));
        return "tasks/view";
    }

    // 📌 форма редактирования
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/edit")
    public String editTask(@PathVariable("id") int id,@AuthenticationPrincipal WorkerDetails workerDetails ,Model model){
        model.addAttribute("task", taskService.showTask(id, workerDetails));
        if (workerDetails.getWorker().getUserRole() == UserRole.ADMIN) {
            model.addAttribute("workers", workerRepository.findAll());
        }
        return "tasks/edit";
    }

    // 📌 обновление
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}")
    public String updateTask(
            @PathVariable int id,
            @ModelAttribute TaskDTO dto,
            @AuthenticationPrincipal WorkerDetails workerDetails
    ) {
        taskService.updateTask(id, dto, workerDetails);
        if (workerDetails.getWorker().getUserRole() == UserRole.ADMIN) {
            return "redirect:/tasks";
        }

        return "redirect:/tasks/my";
    }

    //task soft delete
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/delete")
    public String softDeleteTask(@PathVariable("id") int id, @AuthenticationPrincipal WorkerDetails workerDetails){
        taskService.softDeleteTask(id, workerDetails);;
        if (workerDetails.getWorker().getUserRole() == UserRole.ADMIN) {
            return "redirect:/tasks";
        }

        return "redirect:/tasks/my";
    }

    //task hard delete
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/hard-delete/{id}")
    public String hardDeleteTask(@PathVariable("id") int id, @AuthenticationPrincipal WorkerDetails workerDetails){
        taskService.hardDeleteTask(id, workerDetails);
        return "redirect:/tasks/trash";
    }

    //restore task
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/restore/{id}")
    public String restoreTask(@PathVariable("id") int id, @AuthenticationPrincipal WorkerDetails workerDetails){
        taskService.restoreTask(id, workerDetails);
        if (workerDetails.getWorker().getUserRole() == UserRole.ADMIN) {
            return "redirect:/tasks";
        }

        return "redirect:/tasks/my";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/trash") // URL стал короче, так как @RequestMapping("/tasks") уже есть выше
    public String showTrash(Model model, @PageableDefault(size = 10) Pageable pageable, @AuthenticationPrincipal WorkerDetails workerDetails) {
        // Вызываем сервис с isDeleted = true
        Page<TaskDTO> tasks = taskService.getDeletedTasks(workerDetails, pageable);

        model.addAttribute("tasks", tasks.getContent());
        model.addAttribute("currentPage", pageable.getPageNumber());
        model.addAttribute("totalPages", tasks.getTotalPages());
        if (workerDetails.getWorker().getUserRole() == UserRole.ADMIN) {
            model.addAttribute("workers", workerRepository.findAll());
            model.addAttribute("baseUrl", "/tasks");
        } else {
            model.addAttribute("baseUrl", "/tasks/my");
        }
        return "tasks/trash"; // Убедись, что файл называется trash.html
    }
}