package org.example.Task_Manager.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Task_Manager.DTO.TaskDTO;
import org.example.Task_Manager.Model.Priority;
import org.example.Task_Manager.Model.Status;
import org.example.Task_Manager.Model.UserRole;
import org.example.Task_Manager.Sevice.TaskService;
import org.example.Task_Manager.details.WorkerDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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
    public TaskDTO showTask(@PathVariable("id") int id, @AuthenticationPrincipal WorkerDetails workerDetails ){
        return  taskService.showTask(id, workerDetails);
    }

    // 📌 обновление
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(
            @PathVariable int id,
            @RequestBody TaskDTO dto,
            @AuthenticationPrincipal WorkerDetails workerDetails
    ) {
        TaskDTO updatedDto = taskService.updateTask(id,dto, workerDetails);
        return ResponseEntity.ok(updatedDto);
    }

    //task soft delete
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}")
    public ResponseEntity<Void> softDeleteTask(@PathVariable("id") int id, @AuthenticationPrincipal WorkerDetails workerDetails){
        taskService.softDeleteTask(id, workerDetails);
      return ResponseEntity.ok().build();
    }

    //task hard delete
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> hardDeleteTask(
            @PathVariable int id,
            @AuthenticationPrincipal WorkerDetails workerDetails
    ) {
        taskService.hardDeleteTask(id, workerDetails);
        return ResponseEntity.noContent().build();
    }

    //restore task
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> restoreTask(@PathVariable("id") int id, @AuthenticationPrincipal WorkerDetails workerDetails){

        taskService.restoreTask(id, workerDetails);

        return ResponseEntity.ok().build();
    }
}
