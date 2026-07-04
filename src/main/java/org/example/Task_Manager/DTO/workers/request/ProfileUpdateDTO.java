package org.example.Task_Manager.DTO.workers.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfileUpdateDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @Email
    private String email;
}
