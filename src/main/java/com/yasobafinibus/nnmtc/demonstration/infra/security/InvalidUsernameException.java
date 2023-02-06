package com.yasobafinibus.nnmtc.demonstration.infra.security;

public class InvalidUsernameException extends RuntimeException {

    public InvalidUsernameException(String message) {
        super(message + " is invalid");
    }

}
