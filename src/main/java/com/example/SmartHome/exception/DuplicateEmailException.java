package com.example.SmartHome.exception;

public class DuplicateEmailException extends RuntimeException{
    public DuplicateEmailException(String email){
        super("An account with email " + email + " already exists");
    }
}
