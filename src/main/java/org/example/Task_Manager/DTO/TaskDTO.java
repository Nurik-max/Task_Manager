package org.example.Task_Manager.DTO;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.Task_Manager.Model.Status;
import org.example.Task_Manager.Model.Worker;

import java.time.LocalDateTime;

public class TaskDTO {

    private int id;

    @NotEmpty(message = "Description should not be empty!")
    @Size(min = 2, message = "Size of description should be not less than 2 character!")
    private String description;

     @NotNull
    private LocalDateTime createdDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Integer workerId;
    private String workerName;
    private String workerSurname;

    public TaskDTO(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Integer workerId) {
        this.workerId = workerId;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getWorkerSurname() {
        return workerSurname;
    }

    public void setWorkerSurname(String workerSurname) {
        this.workerSurname = workerSurname;
    }
    // В классе TaskDTO
    public String getWorkerFullName() {
        if (workerName != null && workerSurname != null) {
            return workerName + " " + workerSurname;
        }
        return null;
    }
}
