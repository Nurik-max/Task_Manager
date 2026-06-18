package org.example.Task_Manager.DTO.tasks;

import org.example.Task_Manager.Model.Priority;

public class CreateTaskRequest {

       private String description;
       private Priority priority;
       private Long assignedWorkerId; // только для ADMIN


    public CreateTaskRequest() {
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

    public Long getAssignedWorkerId() {
        return assignedWorkerId;
    }

    public void setAssignedWorkerId(Long assignedWorkerId) {
        this.assignedWorkerId = assignedWorkerId;
    }
}
