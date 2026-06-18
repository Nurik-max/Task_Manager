package org.example.Task_Manager.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Task_Manager.DTO.TaskDTO;
import org.example.Task_Manager.Model.Priority;
import org.example.Task_Manager.Model.Status;
import org.example.Task_Manager.Sevice.TaskService;
import org.example.Task_Manager.details.WorkerDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/tasks")
//@RequiredArgsConstructor
public class TaskRestController {

    private final TaskService taskService;

    public TaskRestController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping //For Admin
    public Page<TaskDTO> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(defaultValue = "false") boolean isDeleted
            ) {

        Pageable pageable = PageRequest.of(page,10);

        //For usual task
        Page<TaskDTO> tasksDTO = taskService.getTasks(status,priority ,keyword,username, start, end, isDeleted, pageable);

        return tasksDTO;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public TaskDTO createTask(
            @RequestBody TaskDTO taskDTO,
            @AuthenticationPrincipal WorkerDetails workerDetails
    ) {
        return taskService.saveTask(taskDTO, workerDetails);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public String showTask(@PathVariable("id") int id, @AuthenticationPrincipal WorkerDetails workerDetails ){


        TaskDTO taskDTO = taskService.showTask(id, workerDetails);
        return taskDTO.toString();
    }
}
