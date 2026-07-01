package be.kdg.integration.gameapplication.model.authservices.services;

import be.kdg.integration.gameapplication.model.databaseaccess.PlayerRegisterDataBase;
import be.kdg.integration.gameapplication.model.user.Player;

import java.sql.SQLException;

/**
 * Handles new player registration.
 * <p>
 * Registers player in database and assigns random name using {@link NicknameGenerator}
 * </p>
 *
 * @see NicknameGenerator
 * @see PasswordEncoder
 * @see PlayerRegisterDataBase
 */

public class RegistrationService{
    private PlayerRegisterDataBase playerRegisterDataBase;
    private PasswordEncoder passwordEncoder;
    private NicknameGenerator nicknameGenerator;

    public RegistrationService(PasswordEncoder passwordEncoder, PlayerRegisterDataBase playerRegisterDataBase, NicknameGenerator nicknameGenerator){
        this.passwordEncoder = passwordEncoder;
        this.playerRegisterDataBase = playerRegisterDataBase;
        this.nicknameGenerator = nicknameGenerator;
    }

    /**
     * Registers new player in a database
     *
     * @param email the player's email address used as a unique login identifier
     * @param password raw password of the player, that will be protected by {@link PasswordEncoder}
     * @return {@link AuthentificationResultEntity} containing the newly registered player and {@link AuthResult#SUCCESS},
     *         or {@code null} player with {@link AuthResult#PLAYER_EXISTS} if email is already taken
     * @throws RegistrationServiceException in case of database exception or {@link PasswordServiceException}
     */

    public AuthentificationResultEntity registerNewPlayer(String email, String password) throws RegistrationServiceException{
        Player player;
        try{
            player = playerRegisterDataBase.findPlayerDataByLogin(email);
        }catch (SQLException e){
            throw new RegistrationServiceException(e.getMessage());
        }
        if(player == null){

            byte[] salt = passwordEncoder.generateSalt();
            String saltString = passwordEncoder.saltToString(salt);

            String hashedSaltedPassword;
            try{
                hashedSaltedPassword = passwordEncoder.addSaltToPasswordAndEncodeIt(password, saltString);
            }catch (PasswordServiceException e) {
                throw e;
            }

            String nickname = nicknameGenerator.generateNickname();

            try{
                playerRegisterDataBase.registerNewPlayer(nickname, email, hashedSaltedPassword, saltString);
                return new AuthentificationResultEntity(playerRegisterDataBase.findPlayerDataByLogin(email), AuthResult.SUCCESS);
            }catch (SQLException e){
                throw new RegistrationServiceException(e.getMessage());
            }
        }
        return new AuthentificationResultEntity(null, AuthResult.PLAYER_EXISTS);
    }
}
