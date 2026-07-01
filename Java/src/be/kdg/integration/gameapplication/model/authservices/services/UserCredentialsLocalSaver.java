package be.kdg.integration.gameapplication.model.authservices.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Handles local saving of password and login of the player.
 *
 * <p>
 * Credentials are saved as plain text in the user's Documents folder
 * under {@code Documents/ZarocGame/savedLogin.zc}.
 * Service will remove file if the player logs out
 * </p>
 *
 */

public class UserCredentialsLocalSaver{
    private final Path PATH_TO_LOGIN_INFO = Paths.get(System.getProperty("user.home"), "Documents", "ZarocGame", "savedLogin.zc");

    /**
     * Saves the player's email and password to a local file.
     * <p>
     * Creates the necessary directories if they do not exist.
     * </p>
     *
     * @param email the player's email address to save
     * @param password the player's raw password to save
     * @throws UserCredentialsLocalSaverException if the file could not be written
     */

    public void saveLoginInfo(String email, String password) throws UserCredentialsLocalSaverException{
        try{
            Files.createDirectories(PATH_TO_LOGIN_INFO.getParent());
            String content = email + System.lineSeparator() + password;
            Files.writeString(PATH_TO_LOGIN_INFO, content);
        }catch (IOException e) {
            throw new UserCredentialsLocalSaverException("Something went wrong while saving login info locally");
        }
    }

    /**
     * Reads saved login credentials from the local file.
     *
     * @return an {@link ArrayList} where index {@code 0} is the email and index {@code 1} is the password or {@code null} if no saved credentials file exists
     * @throws UserCredentialsLocalSaverException if the file could not be read
     */
    public ArrayList<String> getLoginInfo() throws UserCredentialsLocalSaverException{ // email and password
        if(!Files.exists(PATH_TO_LOGIN_INFO)){
            return null;
        }

        try{
            return (ArrayList<String>) Files.readAllLines(PATH_TO_LOGIN_INFO);
        }catch (IOException e) {
            throw new UserCredentialsLocalSaverException("Something went wrong while getting login info from local storage");
        }
    }

    /**
     * Deletes the saved credentials file if it exists.
     * <p>
     * Called during logout to clear saved login data.
     * </p>
     *
     * @throws UserCredentialsLocalSaverException if the file could not be deleted
     */
    public void deleteLoginInfo() throws UserCredentialsLocalSaverException{
        try{
            Files.deleteIfExists(PATH_TO_LOGIN_INFO);
        }catch (IOException e) {
            throw new UserCredentialsLocalSaverException("Something went wrong while removing login info from local storage");
        }
    }
}
