package be.kdg.integration.gameapplication.model.databaseaccess;

import be.kdg.integration.gameapplication.model.user.Player;

import java.sql.SQLException;

public interface PlayerLoginDatabase{
    Player findPlayerDataByLogin(String login) throws SQLException;
}
