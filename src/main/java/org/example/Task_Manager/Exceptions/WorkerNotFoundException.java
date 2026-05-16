package org.example.Task_Manager.Exceptions;

public class WorkerNotFoundException extends RuntimeException {

    public WorkerNotFoundException(int id){

        super("Worker with id: " + id + " not found");
    }

}
