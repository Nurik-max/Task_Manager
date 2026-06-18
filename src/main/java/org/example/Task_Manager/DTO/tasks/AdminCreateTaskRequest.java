package org.example.Task_Manager.DTO.tasks;

import org.example.Task_Manager.Model.Priority;

public class AdminCreateTaskRequest {

    private String description;
    private Priority priority;
    private Integer worker_id;

    public AdminCreateTaskRequest() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getWorker_id() {
        return worker_id;
    }

    public void setWorker_id(Integer worker_id) {
        this.worker_id = worker_id;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
