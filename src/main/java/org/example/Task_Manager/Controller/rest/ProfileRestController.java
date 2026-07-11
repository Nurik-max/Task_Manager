package org.example.Task_Manager.Controller.rest;

import jakarta.validation.Valid;
import org.example.Task_Manager.DTO.tasks.statistics.TaskStatisticsDTO;
import org.example.Task_Manager.DTO.workers.ChangePasswordDTO;
import org.example.Task_Manager.DTO.workers.request.ProfileUpdateDTO;
import org.example.Task_Manager.DTO.workers.response.WorkerDTO;
import org.example.Task_Manager.DTO.workers.response.WorkerProfileResponse;
import org.example.Task_Manager.Sevice.TaskStatisticsService;
import org.example.Task_Manager.Sevice.WorkerService;
import org.example.Task_Manager.details.WorkerDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@PreAuthorize("isAuthenticated()")
public class ProfileRestController {

    private final WorkerService workerService;
    private final TaskStatisticsService taskStatisticsService;

    public ProfileRestController(WorkerService workerService, TaskStatisticsService taskStatisticsService) {
        this.workerService = workerService;
        this.taskStatisticsService = taskStatisticsService;
    }



    @GetMapping
    public ResponseEntity<WorkerProfileResponse> getWorkerProfile(@AuthenticationPrincipal WorkerDetails workerDetails) {

        WorkerDTO workerDTO = workerService.showWorker(workerDetails.getWorker().getId());

        TaskStatisticsDTO statisticsDTO = taskStatisticsService.getStatistics(workerDetails.getWorker().getId());

            WorkerProfileResponse response = new WorkerProfileResponse(workerDTO, statisticsDTO);

        return ResponseEntity.ok(response);
    }


    @PutMapping("/update")
    public ResponseEntity<WorkerDTO> updateWorkerProfile(
            @AuthenticationPrincipal WorkerDetails workerDetails, @Valid @RequestBody ProfileUpdateDTO  profileUpdateDTO) {

       WorkerDTO updatedWorker = workerService.updateProfile(workerDetails.getWorker().getId(), profileUpdateDTO);

       return ResponseEntity.ok(updatedWorker);
    }


    @PostMapping("/password")
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal WorkerDetails workerDetails,
                                               @Valid @RequestBody ChangePasswordDTO dto) {

        workerService.changePassword(workerDetails.getWorker().getId(), dto);
        return ResponseEntity.ok().build();
    }

}
