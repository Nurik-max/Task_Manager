package org.example.Task_Manager.DTO.workers.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Task_Manager.Model.WorkerStatus;

@Getter
@Setter
@NoArgsConstructor
public class ProfileUpdateDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String surname;

    @Email
    private String email;

    @NotBlank
    private String position;

    @NotBlank
    private String phone;

    private WorkerStatus workerStatus;
}
