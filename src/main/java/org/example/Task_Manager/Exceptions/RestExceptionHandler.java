package org.example.Task_Manager.Exceptions;

import org.example.Task_Manager.DTO.tasks.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice(basePackages = "org.example.Task_Manager.Controller.rest")
public class RestExceptionHandler {


    @ExceptionHandler(WorkerNotFoundException.class)
    public ResponseEntity<ErrorResponse>handleException(WorkerNotFoundException ex){

        return ResponseEntity.status(HttpStatus.NOT_FOUND).
                body(new ErrorResponse("WORKER_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse>handleException(TaskNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).
                body(new ErrorResponse("TASK_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            AccessDeniedException ex) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(
                        "ACCESS_DENIED",
                        ex.getMessage()
                ));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        "INTERNAL_ERROR",
                        ex.getMessage()
                ));
    }
}
