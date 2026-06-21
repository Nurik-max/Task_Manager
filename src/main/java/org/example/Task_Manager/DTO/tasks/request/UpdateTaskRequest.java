package org.example.Task_Manager.DTO.tasks.request;

import lombok.Getter;
import lombok.Setter;
import org.example.Task_Manager.Model.Priority;
import org.example.Task_Manager.Model.Status;

@Setter
@Getter
public class UpdateTaskRequest {

    private String description;
    private Priority priority;
    private Status status;

    public UpdateTaskRequest() {
    }

}
