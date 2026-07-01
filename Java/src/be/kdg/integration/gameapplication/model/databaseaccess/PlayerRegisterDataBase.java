package be.kdg.integration.gameapplication.model.databaseaccess;

import java.sql.SQLException;

public interface PlayerRegisterDataBase extends PlayerLoginDatabase{
    void registerNewPlayer(String nickname, String login, String hashedSaltedPassword, String salt) throws SQLException;
}
