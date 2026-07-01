package be.kdg.integration.gameapplication.model.authservices.services;


import be.kdg.integration.gameapplication.model.databaseaccess.PlayerLoginDatabase;
import be.kdg.integration.gameapplication.model.user.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles player authentication including login and logout operations.
 * <p>
 * Validates email format, verifies password against the database and
 * optionally saves or clears login credentials via {@link UserCredentialsLocalSaver}.
 * </p>
 *
 * @see PasswordService
 * @see UserCredentialsLocalSaver
 * @see PlayerLoginDatabase
 */

public class LoginService implements LogoutHandler{
    private PlayerLoginDatabase playerDataBase;
    private PasswordService passwordAuthService;
    private UserCredentialsLocalSaver userCredentialsLocalSaver;


    /**
     * Creates a new {@link LoginService} with the required dependencies.
     *
     * @param userCredentialsLocalSaver handles saving and loading of persisted login credentials
     * @param passwordAuthService handles password hashing and verification
     * @param playerDataBase provides access to player records in the database
     */

    public LoginService(UserCredentialsLocalSaver userCredentialsLocalSaver, PasswordService passwordAuthService, PlayerLoginDatabase playerDataBase){
        this.playerDataBase = playerDataBase;
        this.userCredentialsLocalSaver = userCredentialsLocalSaver;
        this.passwordAuthService = passwordAuthService;
    }

    /**
     * Authenticates a player using email and password.
     * <p>
     * If both {@code email} and {@code passwordToCheck} are {@code null},
     * saved credentials are loaded from {@link UserCredentialsLocalSaver}.
     * The email is validated against a standard format before querying the database.
     * </p>
     *
     * @param email           the player's email address; {@code null} to use saved credentials
     * @param passwordToCheck the plain-text password to verify; {@code null} to use saved credentials
     * @param isSaved         {@code true} to persist the credentials after a successful login
     * @return a {@link AuthentificationResultEntity} containing the authenticated {@link Player} or {@code null} if authentification fails and {@link AuthResult},
     * @throws LoginServiceException when paths of saved credentials are broken, when password service was configured wrong or when database exception occurred
     *
     */

    public AuthentificationResultEntity loginPlayer(String email, String passwordToCheck, boolean isSaved) throws LoginServiceException{

        if(email == null && passwordToCheck == null){
            ArrayList<String> loginInfo;
            try{
                loginInfo = userCredentialsLocalSaver.getLoginInfo();
            }catch (UserCredentialsLocalSaverException e) {
                throw new LoginServiceException(e.getMessage());
            }
            if(loginInfo == null || loginInfo.size() != 2) return null;
            email = loginInfo.get(0);
            passwordToCheck = loginInfo.get(1);
            if(email.isEmpty() || passwordToCheck.isEmpty()) return null;
        }

        Pattern pattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        if(!matcher.matches()) return new AuthentificationResultEntity(null, AuthResult.WRONG_EMAIL_FORMAT);

        Player player;
        try{
            player = playerDataBase.findPlayerDataByLogin(email);
        }catch (SQLException e){
            throw new LoginServiceException(e.getMessage());
        }

        if(player == null) return new AuthentificationResultEntity(null, AuthResult.USER_NOT_FOUND);

        String hashedSaltedPassword = player.getHashedPassword();
        String saltFromDB = player.getSalt();

        try{
            if(!passwordAuthService.confirmPassword(hashedSaltedPassword, saltFromDB, passwordToCheck)){
                return new AuthentificationResultEntity(null, AuthResult.WRONG_PASSWORD);
            }
            if(isSaved){
                userCredentialsLocalSaver.saveLoginInfo(email, passwordToCheck);
            }
            return new AuthentificationResultEntity(player, AuthResult.SUCCESS);
        }catch(PasswordServiceException | UserCredentialsLocalSaverException e) {
            throw new LoginServiceException(e.getMessage());
        }
    }

    /**
     * Logs out the current player by deleting any saved login credentials.
     *
     * @throws LoginServiceException if the credentials could not be deleted due to the broken file paths
     */

    @Override
    public void logOut() throws LoginServiceException{
        try{
            userCredentialsLocalSaver.deleteLoginInfo();
        }catch (UserCredentialsLocalSaverException e) {
            throw new LoginServiceException(e.getMessage());
        }
    }
}
