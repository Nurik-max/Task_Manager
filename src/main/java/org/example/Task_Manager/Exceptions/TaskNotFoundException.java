package org.example.Task_Manager.Exceptions;


public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(int id){
        super("Task with id: " + id + " not found");
    }
}
