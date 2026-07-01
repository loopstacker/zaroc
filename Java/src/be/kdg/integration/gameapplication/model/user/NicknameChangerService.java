package be.kdg.integration.gameapplication.model.user;

import be.kdg.integration.gameapplication.model.databaseaccess.PlayersDatabase;

import java.sql.SQLException;

public class NicknameChangerService {
    private PlayersDatabase playersDatabase;

    public NicknameChangerService(PlayersDatabase playersDatabase){
        this.playersDatabase = playersDatabase;
    }

    public void changeNickname(Player player, String nickname) throws PlayerConfigurationException {
        try {
            playersDatabase.setNewNickname(player.getPlayerID(), nickname);
        } catch (SQLException e) {
            throw new PlayerConfigurationException(e.getMessage());
        }
    }
}
