package org.example.Task_Manager.Exceptions;

public class IncorrectPasswordException extends RuntimeException {

    public IncorrectPasswordException() {
        super("Old password is incorrect");
    }
}
