package org.example.Task_Manager.DTO.workers.request;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Task_Manager.Model.WorkerStatus;

@Getter
@Setter
@NoArgsConstructor
public class ProfileUpdateDTO {


    private String username;


    private String surname;

    @Email
    private String email;

    private String position;

    private String phone;

    private WorkerStatus workerStatus;
}
