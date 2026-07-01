package be.kdg.integration.gameapplication.model.authservices.services;

public class PasswordServiceException extends AuthentificationServiceException{
    public PasswordServiceException(){
        super("Something went wrong in password service");
    }
}
