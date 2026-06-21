package org.example.Task_Manager.DTO.tasks.request;

import lombok.Getter;
import lombok.Setter;
import org.example.Task_Manager.Model.Priority;

@Setter
@Getter
public class CreateTaskRequest {

       private String description;
       private Priority priority;


    public CreateTaskRequest() {
    }

}
