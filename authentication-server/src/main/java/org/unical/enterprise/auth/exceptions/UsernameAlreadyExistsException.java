package org.unical.enterprise.auth.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException {

    private static final String BASE_MESSAGE = "Username already exists";

    public UsernameAlreadyExistsException() { super(BASE_MESSAGE); }

    public UsernameAlreadyExistsException(String message) { super(message); }

    public UsernameAlreadyExistsException(Throwable cause) { super(BASE_MESSAGE, cause); }

    public UsernameAlreadyExistsException(String message, Throwable cause) { super(message, cause); }

}
