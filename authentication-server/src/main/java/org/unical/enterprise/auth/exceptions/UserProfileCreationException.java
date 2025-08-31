package org.unical.enterprise.auth.exceptions;

public class UserProfileCreationException extends RuntimeException {

    private static final String BASE_MESSAGE = "User registration failed on Utente-Service";

    public UserProfileCreationException() { super(BASE_MESSAGE); }

    public UserProfileCreationException(String message) { super(message); }

    public UserProfileCreationException(Throwable cause) { super(BASE_MESSAGE, cause); }

    public UserProfileCreationException(String message, Throwable cause) { super(message, cause); }



}
