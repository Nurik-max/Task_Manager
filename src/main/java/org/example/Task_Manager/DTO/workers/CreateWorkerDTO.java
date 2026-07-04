package org.example.Task_Manager.DTO.workers;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CreateWorkerDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String surname;

    @NotBlank
    private String position;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String confirmPassword;

    private LocalDateTime createdAt;

    public CreateWorkerDTO() {
    }

}