package org.example.Task_Manager.DTO.tasks.request;

import org.example.Task_Manager.Model.Priority;
import org.example.Task_Manager.Model.Status;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class AdminUpdateTaskRequest {

        private String description;
        private Priority priority;
        private Status status;
        private Integer workerId;

    public AdminUpdateTaskRequest() {
    }

}
