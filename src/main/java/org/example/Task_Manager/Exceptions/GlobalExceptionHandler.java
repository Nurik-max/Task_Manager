package org.example.Task_Manager.Exceptions;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) //404 - error
    public String handleTaskNotFound(TaskNotFoundException ex, Model model){
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/404"; //Путь к файлу 404.html в папке templates/error
    }

    @ExceptionHandler(WorkerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404 - error
    public String handleWorkerNotFound(WorkerNotFoundException ex, Model model){

        model.addAttribute("errorCode", "500"); // Добавляем код ошибки
        model.addAttribute("errorMessage", "Произошла внутренняя ошибка сервера.");
        return "error/500";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationExceptions(MethodArgumentNotValidException ex, Model model) {
        model.addAttribute("errorCode", "400");
        model.addAttribute("errorMessage", "Данные заполнены неверно. Проверьте правильность ввода.");
        return "error/404"; // Можно использовать 404.html или создать 400.html
    }
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String handleDatabaseError(DataAccessException ex, Model model) {
        model.addAttribute("errorCode", "503");
        model.addAttribute("errorMessage", "Проблема с доступом к базе данных. Попробуйте позже.");
        return "error/500";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleAllUncaughtErrors(Exception ex, Model model) {
        // В консоль выводим реальную ошибку, чтобы ты мог её найти и починить
        ex.printStackTrace();

        model.addAttribute("errorCode", "500");
        model.addAttribute("errorMessage", "Что-то пошло совсем не так. Мы уже разбираемся.");
        return "error/500";
    }
    }

