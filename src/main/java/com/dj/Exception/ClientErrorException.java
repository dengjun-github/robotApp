package com.dj.Exception;

public class ClientErrorException extends RuntimeException {

    public ClientErrorException(String msg) {
        super(msg);
    }
}
