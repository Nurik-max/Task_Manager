package org.example.Task_Manager.Controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.Task_Manager.DTO.tasks.TaskDTO;
import org.example.Task_Manager.DTO.tasks.request.AdminCreateTaskRequest;
import org.example.Task_Manager.DTO.tasks.request.AdminUpdateTaskRequest;
import org.example.Task_Manager.DTO.tasks.request.CreateTaskRequest;
import org.example.Task_Manager.DTO.tasks.request.UpdateTaskRequest;
import org.example.Task_Manager.DTO.tasks.response.AdminResponse;
import org.example.Task_Manager.DTO.tasks.response.UserResponse;
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
import org.springframework.ui.Model;
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
    public ResponseEntity<Page<AdminResponse>>getAllTasks(
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
        Page<AdminResponse> adminResponses = adminTaskService.getTasks(status,priority ,keyword,username, start, end, isDeleted, pageable);

        return ResponseEntity.ok(adminResponses);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my")
    public ResponseEntity<Page<UserResponse>> getAllMyTasks( @AuthenticationPrincipal WorkerDetails workerDetails,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(required = false) Status status,
                                 @RequestParam(required = false) Priority priority,
                                 @RequestParam(required = false) String keyword,
                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
                                 @RequestParam(defaultValue = "false") boolean isDeleted,
                                 Model model){

        Pageable pageable = PageRequest.of(page,10);


        Page<UserResponse> taskDTOPage = userTaskService.workerListOfTask(workerDetails,status,priority ,keyword, start, end, pageable, isDeleted);


        return ResponseEntity.ok(taskDTOPage);

    }

    @PostMapping("/admin")//For Admin
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminResponse> createTask(
            @RequestBody AdminCreateTaskRequest taskDTO,
            @AuthenticationPrincipal WorkerDetails workerDetails
    ) {
        AdminResponse savedAdminTaskDTO = adminTaskService.saveTask(taskDTO, workerDetails);
        return ResponseEntity.ok(savedAdminTaskDTO);
    }

    @PostMapping("/user")//For Users
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> createMyTasks(@RequestBody CreateTaskRequest request, @AuthenticationPrincipal WorkerDetails workerDetails) {

        UserResponse savedUserTaskDTO = userTaskService.saveUserTask(request,workerDetails);
        return ResponseEntity.ok(savedUserTaskDTO);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public AdminResponse showTask(@PathVariable("id") int id, @AuthenticationPrincipal WorkerDetails workerDetails ){
        return  adminTaskService.showTask(id, workerDetails);
    }

    // 📌 обновление
    @PreAuthorize("hasRole('ADMIN')")//For Admin
    @PatchMapping("/{id}/admin")
    public ResponseEntity<AdminResponse> updateTask(
            @PathVariable int id,
            @RequestBody AdminUpdateTaskRequest dto,
            @AuthenticationPrincipal WorkerDetails workerDetails
    ) {
        AdminResponse updatedDto = adminTaskService.updateTask(id,dto, workerDetails);
        return ResponseEntity.ok(updatedDto);
    }

    @PreAuthorize("isAuthenticated()")//For Users
    @PatchMapping("/{id}/user")
    public ResponseEntity<UserResponse> updateMyTask(@PathVariable int id,
                                                @RequestBody UpdateTaskRequest updateTaskRequest,
                                                @AuthenticationPrincipal WorkerDetails workerDetails){

        UserResponse updatedDTO = userTaskService.updateUserTask(id, updateTaskRequest, workerDetails);
        return ResponseEntity.ok(updatedDTO);
    }

    //task soft delete
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/soft-delete")
    public ResponseEntity<Void> softDeleteTask(@PathVariable("id") int id, @AuthenticationPrincipal WorkerDetails workerDetails){
        adminTaskService.softDeleteTask(id, workerDetails);
      return ResponseEntity.ok().build();
    }

    //task hard delete
    @Operation(summary = "Delete task")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/hard-delete")
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
    public ResponseEntity<Page<AdminResponse>> showTrash(   @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(required = false) Status status,
                                                            @RequestParam(required = false) Priority priority,
                                                            @RequestParam(required = false) String keyword,
                                                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
                                                            @RequestParam(defaultValue = "true") boolean isDeleted, @AuthenticationPrincipal WorkerDetails workerDetails) {

        Pageable pageable = PageRequest.of(page,10);

        //For usual task
        Page<AdminResponse> adminResponses = adminTaskService.getDeletedTasks(status, priority, keyword, start, end, isDeleted, pageable,workerDetails);

        // Вызываем сервис с isDeleted = true

        return ResponseEntity.ok(adminResponses); // Убедись, что файл называется trash.html
    }
}
