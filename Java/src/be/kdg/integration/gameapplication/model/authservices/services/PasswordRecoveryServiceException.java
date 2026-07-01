package be.kdg.integration.gameapplication.model.authservices.services;

public class PasswordRecoveryServiceException extends AuthentificationServiceException{
    public PasswordRecoveryServiceException(String message){
        super(message);
    }

    public PasswordRecoveryServiceException(){
        super("Something went wrong during recovering password");
    }
}
