package org.example.Task_Manager.DTO.workers;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangePasswordDTO {

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String confirmPassword;

    public ChangePasswordDTO(){

    }

}
