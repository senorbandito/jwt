package com.alibou.security.exception;

public class MessageNotReadableException extends RuntimeException{

    public MessageNotReadableException(){
        super("Data sent is not valid. Please check again.");
    }
}