package be.kdg.integration.gameapplication.model.databaseaccess;

import be.kdg.integration.gameapplication.model.user.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayersDatabase implements PlayerLoginDatabase, PlayerRegisterDataBase, PasswordChangerDatabase{
    private Connection connection;
    private DataBaseConnection dataBaseConnection;

    public PlayersDatabase(DataBaseConnection dataBaseConnection){
        this.connection = dataBaseConnection.getConnection();
        this.dataBaseConnection = dataBaseConnection;
    }

    @Override
    public Player findPlayerDataByLogin(String email) throws SQLException {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT hashed_password, player_id, nickname, (SELECT points FROM v_points_per_player WHERE player_id = p.player_id), salt FROM players p WHERE email = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            );

            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()) return null;

            String hashedSaltedPassword = resultSet.getString(1);
            int id = resultSet.getInt(2);

            String nick = resultSet.getString(3);
            int points = resultSet.getInt(4);
            String salt = resultSet.getString(5);
            resultSet.close();
            preparedStatement.close();
            return new Player(email, hashedSaltedPassword, salt, id, nick, points, new ConfigsDatabase(dataBaseConnection));
        }catch (SQLException e) {
            throw e;
        }
    }

    public void registerNewPlayer(String nickname ,String login, String hashedSaltedPassword, String salt) throws SQLException{
        try{
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO players(nickname, email, hashed_password, salt) VALUES (?,?,?,?)"
            );
            statement.setString(1, nickname);
            statement.setString(2, login);
            statement.setString(3, hashedSaltedPassword);
            statement.setString(4, salt);
            statement.executeUpdate();
            statement.close();
        }catch (SQLException e) {
            throw new SQLException("Unable to register player");
        }
    }

    @Override
    public void setNewPassword(String email, String hashedPassword, String salt) throws SQLException{
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE players SET hashed_password = ?, salt = ? WHERE email = ?"
            );
            preparedStatement.setString(1, hashedPassword);
            preparedStatement.setString(2, salt);
            preparedStatement.setString(3, email);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (SQLException e){
            throw new SQLException("Unable to change password");
        }
    }

    public void setNewNickname(int playerID, String nickname) throws SQLException {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE players SET nickname = ? WHERE player_id = ?"
            );
            preparedStatement.setString(1, nickname);
            preparedStatement.setInt(2, playerID);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (SQLException e){
            throw new SQLException("Unable to change username");
        }
    }
}
