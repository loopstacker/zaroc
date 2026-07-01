package be.kdg.integration.gameapplication.model.authservices.services;

public class AuthentificationServiceException extends RuntimeException{
    public AuthentificationServiceException(String message){
        super("Check your connection: " + message);
    }
}
