package org.example.Task_Manager.DTO.tasks;

import org.example.Task_Manager.Model.Priority;
import org.example.Task_Manager.Model.Status;

public class AdminUpdateTaskRequest {

        private String description;
        private Priority priority;
        private Status status;
        private Integer updatedWorkerId;

    public AdminUpdateTaskRequest() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Integer getUpdatedWorkerId() {
        return updatedWorkerId;
    }

    public void setUpdatedWorkerId(Integer updatedWorkerId) {
        this.updatedWorkerId = updatedWorkerId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
