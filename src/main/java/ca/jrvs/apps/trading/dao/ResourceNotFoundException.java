package ca.jrvs.apps.trading.dao;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(Exception e) {
        super(e.getMessage());
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

}