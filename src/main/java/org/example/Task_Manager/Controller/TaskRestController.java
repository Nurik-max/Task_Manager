package org.example.Task_Manager.Controller;

import org.example.Task_Manager.DTO.TaskDTO;
import org.example.Task_Manager.DTO.tasks.AdminCreateTaskRequest;
import org.example.Task_Manager.DTO.tasks.AdminUpdateTaskRequest;
import org.example.Task_Manager.DTO.tasks.CreateTaskRequest;
import org.example.Task_Manager.DTO.tasks.UpdateTaskRequest;
import org.example.Task_Manager.Model.Priority;
import org.example.Task_Manager.Model.Status;
import org.example.Task_Manager.Sevice.AdminTaskService;
import org.example.Task_Manager.Sevice.UserTaskService;
import org.example.Task_Manager.details.WorkerDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/tasks")
//@RequiredArgsConstructor
public class TaskRestController {

    private final AdminTaskService adminTaskService;
    private final UserTaskService userTaskService;

    public TaskRestController(AdminTaskService adminTaskService, UserTaskService userTaskService) {
        this.adminTaskService = adminTaskService;
        this.userTaskService = userTaskService;
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
        Page<TaskDTO> tasksDTO = adminTaskService.getTasks(status,priority ,keyword,username, start, end, isDeleted, pageable);

        return tasksDTO;
    }

    @PostMapping("/admin")//For Admin
    @PreAuthorize("hasRole('ADMIN')")
    public TaskDTO createTask(
            @RequestBody AdminCreateTaskRequest taskDTO,
            @AuthenticationPrincipal WorkerDetails workerDetails
    ) {
        return adminTaskService.saveTask(taskDTO, workerDetails);
    }

    @PostMapping("/user")//For Users
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TaskDTO> createMyTasks(@RequestBody CreateTaskRequest request, @AuthenticationPrincipal WorkerDetails workerDetails) {

        TaskDTO savedUserTaskDTO = userTaskService.saveUserTask(request,workerDetails);
        return ResponseEntity.ok(savedUserTaskDTO);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public TaskDTO showTask(@PathVariable("id") int id, @AuthenticationPrincipal WorkerDetails workerDetails ){
        return  adminTaskService.showTask(id, workerDetails);
    }

    // 📌 обновление
    @PreAuthorize("hasRole('ADMIN'")//For Admin
    @PatchMapping("/{id}/admin")
    public ResponseEntity<TaskDTO> updateTask(
            @PathVariable int id,
            @RequestBody AdminUpdateTaskRequest dto,
            @AuthenticationPrincipal WorkerDetails workerDetails
    ) {
        TaskDTO updatedDto = adminTaskService.updateTask(id,dto, workerDetails);
        return ResponseEntity.ok(updatedDto);
    }

    @PreAuthorize("isAuthenticated()")//For Users
    @PatchMapping("/{id}/user")
    public ResponseEntity<TaskDTO> updateMyTask(@PathVariable int id, UpdateTaskRequest updateTaskRequest,
                                                @AuthenticationPrincipal WorkerDetails workerDetails){

        TaskDTO updatedDTO = userTaskService.updateUserTask(id, updateTaskRequest, workerDetails);
        return ResponseEntity.ok(updatedDTO);
    }

    //task soft delete
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}")
    public ResponseEntity<Void> softDeleteTask(@PathVariable("id") int id, @AuthenticationPrincipal WorkerDetails workerDetails){
        adminTaskService.softDeleteTask(id, workerDetails);
      return ResponseEntity.ok().build();
    }

    //task hard delete
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> hardDeleteTask(
            @PathVariable int id,
            @AuthenticationPrincipal WorkerDetails workerDetails
    ) {
        adminTaskService.hardDeleteTask(id, workerDetails);
        return ResponseEntity.noContent().build();
    }

    //restore task
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}/restore")
    public ResponseEntity<Void> restoreTask(@PathVariable("id") int id, @AuthenticationPrincipal WorkerDetails workerDetails){

        adminTaskService.restoreTask(id, workerDetails);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/trash") // URL стал короче, так как @RequestMapping("/tasks") уже есть выше
    public ResponseEntity<Page<TaskDTO>> showTrash(@PageableDefault(size = 10) Pageable pageable, @AuthenticationPrincipal WorkerDetails workerDetails) {
        // Вызываем сервис с isDeleted = true
        Page<TaskDTO> tasks = adminTaskService.getDeletedTasks(workerDetails, pageable);

        return ResponseEntity.ok(tasks); // Убедись, что файл называется trash.html
    }
}
