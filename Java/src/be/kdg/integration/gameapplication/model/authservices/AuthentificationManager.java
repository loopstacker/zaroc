package be.kdg.integration.gameapplication.model.authservices;

import be.kdg.integration.gameapplication.model.authservices.services.*;
import be.kdg.integration.gameapplication.model.databaseaccess.DataBaseConnection;
import be.kdg.integration.gameapplication.model.databaseaccess.DataBaseChecker;
import be.kdg.integration.gameapplication.model.databaseaccess.PlayersDatabase;
import be.kdg.integration.gameapplication.model.user.NicknameChangerService;

import java.sql.SQLException;

/**
 * It Initializes the authentification classes and creates connection
 *
 * @see DataBaseConnection
 * @see LoginService
 * @see RegistrationService
 * @see PasswordRecoveryService
 * @see NicknameChangerService
 */


public class AuthentificationManager{
    private DataBaseConnection dataBaseConnection;
    private LoginService loginService;
    private RegistrationService registrationService;
    private PasswordRecoveryService passwordRecoveryService;
    private NicknameChangerService nicknameChangerService;


    /**
     * Creates connection for player
     *
     * <p>Initialization order:
     * <ol>
     *   <li>{@link DataBaseConnection} establishes the database connection</li>
     *   <li>{@link DataBaseChecker} verifies the database structure</li>
     *   <li>{@link PlayersDatabase} provides access to players data</li>
     *   <li>{@link MailService} sends confirmation emails to players</li>
     *   <li>{@link PasswordService} handles password changes or creation</li>
     *   <li>{@link UserCredentialsLocalSaver} manages local credential storage</li>
     *   <li>{@link NicknameGenerator} generates default nickname</li>
     * </ol>
     *</p>
     * @throws AuthentificationServiceException if database is not available or player has no connection
     */
    public void createConnection() throws AuthentificationServiceException{
        try{
            //in a case where at least one of steps below fails
            //there would be no access to authentification for player
            //it will just simply say no connection

            this.dataBaseConnection = new DataBaseConnection();

            new DataBaseChecker(dataBaseConnection);
            PlayersDatabase playersDataBase = new PlayersDatabase(dataBaseConnection);
            PasswordService passwordAuthService = new PasswordService();
            UserCredentialsLocalSaver userCredentialsLocalSaver = new UserCredentialsLocalSaver();

            this.passwordRecoveryService = new PasswordRecoveryService(new MailService(), passwordAuthService,playersDataBase);
            this.loginService = new LoginService(userCredentialsLocalSaver,passwordAuthService,playersDataBase);

            NicknameGenerator nicknameGenerator = new NicknameGenerator();
            this.registrationService = new RegistrationService(passwordAuthService, playersDataBase, nicknameGenerator);

            this.nicknameChangerService = new NicknameChangerService(playersDataBase);
        }catch (SQLException e) {
            throw new AuthentificationServiceException(e.getMessage());
        }
    }

    /**
     * Closes connection with DB if one was present
     *
     * @throws AuthentificationServiceException if closing connection fails
     */
    public void closeConnection() throws AuthentificationServiceException{
        if(this.dataBaseConnection != null){
            try{
                dataBaseConnection.closeConnection();
            }catch (SQLException e) {
                throw new AuthentificationServiceException(e.getMessage());
            }
        }
    }

    public PasswordRecoveryService getPasswordRecoveryService(){
        return passwordRecoveryService;
    }

    public DataBaseConnection getDataBaseConnection(){
        return dataBaseConnection;
    }

    public LoginService getLoginService(){
        return loginService;
    }

    public RegistrationService getRegistrationService(){
        return registrationService;
    }

    public NicknameChangerService getNicknameChangerService() {
        return nicknameChangerService;
    }
}
