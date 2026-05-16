package org.example.Task_Manager.Controller;

import jakarta.validation.Valid;
import org.example.Task_Manager.DTO.TaskDTO;
import org.example.Task_Manager.Model.Status;
import org.example.Task_Manager.Repoitory.TaskRepository;
import org.example.Task_Manager.Repoitory.WorkerRepository;
import org.example.Task_Manager.Sevice.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
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

    @GetMapping
    public String getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String name,
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
          }
      }
// 🔥 Весь твой сложный if-else заменяется одной строчкой!

      //For usual task
      Page<TaskDTO> tasksDTO = taskService.getTasks(statusEnum, keyword,name, start, end, isDeleted, pageable);


        model.addAttribute("tasks", tasksDTO.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", tasksDTO.getTotalPages());
        // Прокидываем фильтры обратно в модель, чтобы сохранить их в полях поиска на странице
        model.addAttribute("selectedStatus", status);
        model.addAttribute("keyword", keyword);
        model.addAttribute("name", name);
        model.addAttribute("createdDate", start);


        return "tasks/tasks";
    }

    // 📌 форма создания
    @GetMapping("/new")
    public String newTask(Model model){

        model.addAttribute("task", new TaskDTO());

        model.addAttribute("workers", workerRepository.findAll());
        return "tasks/new";
    }

    // 📌 создание
    @PostMapping
    public String createNewTask(@ModelAttribute("task") @Valid TaskDTO taskDTO,
                                BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "tasks/new";
        }
        taskDTO.setCreatedDate(java.time.LocalDateTime.now());
        taskService.saveTask(taskDTO);
        return "redirect:/tasks";
    }

    // 📌 показать задачу
    @GetMapping("/{id}")
    public String showTask(@PathVariable("id") int id, Model model){
        model.addAttribute("task", taskService.showTask(id));
        return "tasks/view";
    }

    // 📌 форма редактирования
    @GetMapping("/{id}/edit")
    public String editTask(@PathVariable("id") int id, Model model){
        model.addAttribute("task", taskService.showTask(id));
        model.addAttribute("workers", workerRepository.findAll());
        return "tasks/edit";
    }

    // 📌 обновление
    @PatchMapping("/{id}")
    public String updateTask(@ModelAttribute("task") @Valid TaskDTO taskDTO,
                             BindingResult bindingResult, @PathVariable("id") int id){
        if(bindingResult.hasErrors()){
            return "tasks/edit";
        }
        taskService.updateTask(id, taskDTO);
        return "redirect:/tasks";
    }

    //task soft delete
    @PostMapping("/{id}")
    public String softDeleteTask(@PathVariable("id") int id){
        taskService.softDeleteTask(id);;
        return "redirect:/tasks";
    }

    //task hard delete
    @PostMapping("/hard-delete/{id}")
    public String hardDeleteTask(@PathVariable("id") int id){
        taskService.hardDeleteTask(id);
        return "redirect:/trash";
    }

    //restore task
    @PostMapping("/restore/{id}")
    public String restoreTask(@PathVariable("id") int id){
        taskService.restoreTask(id);
        return "redirect:/tasks";
    }

    @GetMapping("/trash") // URL стал короче, так как @RequestMapping("/tasks") уже есть выше
    public String showTrash(Model model, @PageableDefault(size = 10) Pageable pageable) {
        // Вызываем сервис с isDeleted = true
        Page<TaskDTO> tasks = taskService.getTasks(null, null, null, null, null,true, pageable);

        model.addAttribute("tasks", tasks.getContent());
        model.addAttribute("currentPage", pageable.getPageNumber());
        model.addAttribute("totalPages", tasks.getTotalPages());
        return "tasks/trash"; // Убедись, что файл называется trash.html
    }
}