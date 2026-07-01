package be.kdg.integration.gameapplication.model.databaseaccess;

import be.kdg.integration.gameapplication.model.user.Player;

import java.sql.SQLException;

public interface PasswordChangerDatabase{
    Player findPlayerDataByLogin(String login) throws SQLException;
    void setNewPassword(String email, String password, String salt) throws SQLException;
}
