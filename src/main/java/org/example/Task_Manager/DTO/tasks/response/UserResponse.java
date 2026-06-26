package org.example.Task_Manager.DTO.tasks.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Task_Manager.Model.Priority;
import org.example.Task_Manager.Model.Status;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
public class UserResponse {


    private int id;
    private String description;
    private Status status;
    private Priority priority;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    private String workerUsername; // только если это его задача или назначено


}
