package be.kdg.integration.gameapplication.model.authservices.services;

import be.kdg.integration.gameapplication.model.user.Player;

/**
 * Entity that is used to keep validation result of LoginService and return player
 *
 * @see LoginService
 * @see Player
 * @see AuthResult
 */
public class AuthentificationResultEntity{
    private final AuthResult authResult;
    private final Player player;

    /**
     * Creates an AuthentificationResultEntity
     *
     * @param player is a returned player, {@code null} if no player were found
     * @param authResult is result of authentification.
     */
    public AuthentificationResultEntity(Player player, AuthResult authResult){
        this.player = player;
        this.authResult = authResult;
    }

    public AuthResult getAuthResult(){
        return authResult;
    }

    public Player getPlayer(){
        return player;
    }
}
