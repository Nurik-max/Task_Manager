package org.example.Task_Manager.DTO.tasks.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter

public class ErrorResponse {
    private String error;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(String error, String message){
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
