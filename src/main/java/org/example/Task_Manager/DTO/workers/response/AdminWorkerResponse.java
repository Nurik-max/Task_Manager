package org.example.Task_Manager.DTO.workers.response;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Task_Manager.Model.UserRole;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
public class AdminWorkerResponse {

    private String username;

    private String surname;

    private String position;

    private String email;

    private String password;

    private UserRole userRole;

    private LocalDateTime createdAt;
}
