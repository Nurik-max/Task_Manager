package org.example.Task_Manager.DTO.workers;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.example.Task_Manager.Model.UserRole;
import org.example.Task_Manager.Model.WorkerStatus;

import java.time.LocalDateTime;

@Setter
@Getter
public class AdminCreateWorkerDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String surname;

    @NotBlank
    private String position;

    @NotBlank
    private String email;

    private String phone;

    @NotBlank
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private LocalDateTime createdAt;

    public AdminCreateWorkerDTO() {
    }


}
