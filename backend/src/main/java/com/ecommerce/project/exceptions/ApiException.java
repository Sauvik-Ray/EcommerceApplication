package com.ecommerce.project.exceptions;

public class ApiException extends RuntimeException{
    private static final long serialVersionUTD = 1L;

    public ApiException(){

    }

    public ApiException(String message){
        super(message);
    }
}
