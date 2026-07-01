package be.kdg.integration.gameapplication.model.authservices.services;

import java.io.IOException;

/**
 * Defines the contract for logging out a player.
 *
 * @see LoginService
 */

public interface LogoutHandler{
    /**
     * Logs out the current player.
     *
     * @throws IOException if logout data could not be saved locally
     */

    void logOut() throws IOException;
    
}
