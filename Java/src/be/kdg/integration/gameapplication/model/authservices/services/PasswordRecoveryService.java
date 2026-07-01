package be.kdg.integration.gameapplication.model.authservices.services;

import be.kdg.integration.gameapplication.model.databaseaccess.PasswordChangerDatabase;
import be.kdg.integration.gameapplication.model.user.Player;
import jakarta.mail.MessagingException;

import java.sql.SQLException;

/**
 * Service that is responsible for changing password
 * <p>
 *     Send the request to send the check code to validate accounts owner, confirms it and changes the password in database
 * </p>
 *
 * @see MailService
 * @see PasswordChangerDatabase
 * @see PasswordEncoder
 */

public class PasswordRecoveryService{
    private MailService mailService;
    private String code;
    private PasswordChangerDatabase passwordChangerDatabase;
    private PasswordEncoder passwordEncoder;

    public PasswordRecoveryService(MailService mailService, PasswordEncoder passwordEncoder, PasswordChangerDatabase passwordChangerDatabase){
        this.mailService = mailService;
        this.passwordChangerDatabase = passwordChangerDatabase;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Checks whether a player account with the given email exists in the database.
     *
     * @param email is unique identifier of player, that is used to check whether the account exists
     * @return {@code true} if the account exists, {@code false} otherwise
     */

    public boolean checkIfExists(String email) throws SQLException {
        Player player = passwordChangerDatabase.findPlayerDataByLogin(email);
        return player != null;
    }

    /**
     * Hashes a raw password using {@link PasswordEncoder} and sends the request to update password in database
     * @param email unique player identifier
     * @param newPassword new raw password
     * @throws PasswordRecoveryServiceException if the hasher is misconfigured or a database error occurs
     */

    public void setNewPassword(String email, String newPassword) throws PasswordRecoveryServiceException{
        String saltAsString = passwordEncoder.generateSaltAsString();
        String saltedPassword;

        try{
            saltedPassword = passwordEncoder.addSaltToPasswordAndEncodeIt(newPassword, saltAsString);
        }catch (PasswordServiceException e) {
            throw new PasswordRecoveryServiceException(e.getMessage());
        }

        try{
            passwordChangerDatabase.setNewPassword(email, saltedPassword, saltAsString);
        }catch (SQLException e) {
            throw new PasswordRecoveryServiceException(e.getMessage());
        }
    }

    /**
     * Sends 5 letters code to the provided email using {@link MailService} and saves is as a local variable
     *
     * @param mail the address that code will be sent to
     * @throws PasswordRecoveryServiceException if mail was not found or if game-mail access was not provided to the {@link MailService}
     */
    public void sendRecoveryCode(String mail) throws PasswordRecoveryServiceException{
        try{
            this.code = mailService.sendForgetPasswordRequest(mail);
        }catch (MessagingException e) {
            throw new PasswordRecoveryServiceException(e.getMessage());
        }
    }

    /**
     * Checks whether the code entered by the user matches the one that was sent to his email.
     *
     * @param codeFromUser the recovery code entered by the user
     * @return {@code true} if the code matches, {@code false} otherwise
     */
    public boolean checkCode(String codeFromUser){
        return code.equals(codeFromUser);
    }
}
