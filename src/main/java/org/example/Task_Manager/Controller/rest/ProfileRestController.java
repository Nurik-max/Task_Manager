package org.example.Task_Manager.Controller.rest;

import jakarta.validation.Valid;
import org.example.Task_Manager.DTO.tasks.statistics.TaskStatisticsDTO;
import org.example.Task_Manager.DTO.workers.ChangePasswordDTO;
import org.example.Task_Manager.DTO.workers.request.ProfileUpdateDTO;
import org.example.Task_Manager.DTO.workers.response.WorkerDTO;
import org.example.Task_Manager.DTO.workers.response.WorkerProfileResponse;
import org.example.Task_Manager.Exceptions.WorkerNotFoundException;
import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Repository.WorkerMapper;
import org.example.Task_Manager.Repository.WorkerRepository;
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
    private final WorkerRepository workerRepository;
    private final TaskStatisticsService taskStatisticsService;
    private final WorkerMapper workerMapper;

    public ProfileRestController(WorkerService workerService, WorkerRepository workerRepository, TaskStatisticsService taskStatisticsService, WorkerMapper workerMapper) {
        this.workerService = workerService;
        this.workerRepository = workerRepository;
        this.taskStatisticsService = taskStatisticsService;
        this.workerMapper = workerMapper;
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

        Worker existingWorker = workerRepository.findById(workerDetails.getWorker().getId()).orElseThrow(()
                -> new WorkerNotFoundException(workerDetails.getWorker().getId()));
        workerMapper.updateProfileFromDTO(profileUpdateDTO, existingWorker);
        Worker updatedWorker = workerRepository.save(existingWorker);

       return ResponseEntity.ok(workerMapper.toUserResponse(updatedWorker));
    }


    @PostMapping("/password")
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal WorkerDetails workerDetails,
                                               @Valid @RequestBody ChangePasswordDTO dto) {

        workerService.changePassword(workerDetails.getWorker().getId(), dto);
        return ResponseEntity.ok().build();
    }

}
