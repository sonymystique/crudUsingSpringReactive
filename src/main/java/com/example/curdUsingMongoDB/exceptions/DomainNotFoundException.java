package com.example.curdUsingMongoDB.exceptions;
public class DomainNotFoundException extends RuntimeException{
    public DomainNotFoundException(String message){
        super(message);
    }
}
