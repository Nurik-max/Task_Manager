package org.example.Task_Manager.DTO.tasks;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.Task_Manager.Model.Priority;
import org.example.Task_Manager.Model.Status;
import org.example.Task_Manager.Model.Worker;

import java.time.LocalDateTime;

@Setter
@Getter
public class TaskDTO {

    private int id;

    @NotBlank(message = "Description should not be empty!")
    @Size(min = 2, max = 255, message = "Size of description should be not less than 2 character!")
    private String description;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private LocalDateTime deletedAt;

    private Status status;
    @NotNull(message = "Please select priority")
    private Priority priority;


    private Integer workerId;
    private String workerUsername;
    private String workerSurname;


    public TaskDTO(){}

    // В классе TaskDTO
    public String getWorkerFullName() {
        if (workerUsername != null && workerSurname != null) {
            return workerUsername + " " + workerSurname;
        }
        return null;
    }

}
