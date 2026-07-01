package be.kdg.integration.gameapplication.model.databaseaccess;

import be.kdg.integration.gameapplication.model.settings.Config;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigsDatabase {
    private Connection connection;

    public ConfigsDatabase(DataBaseConnection dataBaseConnection){
        this.connection = dataBaseConnection.getConnection();
    }

    public ArrayList<Config> getConfigs(int playerId) throws SQLException{
        try{
            correctAmountOfConfigs(playerId);
            return sendRequestForConfigs(playerId);
        }catch (SQLException e) {
            throw e;
        }
    }

    public void updateConfigs(int playerId, List<Config> configs) throws SQLException{
        try{
            correctAmountOfConfigs(playerId);
            insertUpdates(playerId, configs);
        }catch (SQLException e) {
            throw e;
        }
    }

    private ArrayList<Config> sendRequestForConfigs(int playerId) throws SQLException{
        ArrayList<Config> configs = new ArrayList<>();
        String sql = """
                    SELECT *
                    FROM v_configs_per_player
                    WHERE player_id = ?
                    ORDER BY config_id;
                    """;
        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, playerId);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                String firstLeftColor = resultSet.getString("first_left_color");
                String firstRightColor = resultSet.getString("first_right_color");
                String secondLeftColor = resultSet.getString("second_left_color");
                String secondRightColor = resultSet.getString("second_right_color");
                String thirdLeftColor = resultSet.getString("third_left_color");
                String thirdRightColor = resultSet.getString("third_right_color");
                String playerPieceColor = resultSet.getString("player_piece_color");
                String enemyPieceColor = resultSet.getString("enemy_piece_color");
                String configName = resultSet.getString("config_name");
                double musicVolume = resultSet.getDouble("music_volume");
                double effectsVolume = resultSet.getDouble("effects_volume");
                String screenSize = resultSet.getString("screen_size_preferences");
                String songFlags = resultSet.getString("enabled_songs_flags");
                configs.add(new Config(configName, playerPieceColor, enemyPieceColor, musicVolume, effectsVolume, screenSize, firstLeftColor, secondLeftColor, thirdLeftColor, firstRightColor, secondRightColor, thirdRightColor, songFlags));
            }
            resultSet.close();
            statement.close();
            return configs;
        }catch (SQLException e) {
            throw new SQLException("Something went wrong when getting configs");
        }
    }

    private void correctAmountOfConfigs(int playerId) throws SQLException{
        try{
            addConfigsUntilFive(checkIfPlayerHasEnoughConfigs(playerId), playerId);
        }catch (SQLException e) {
            throw e;
        }
    }

    private void insertUpdates(int playerId, List<Config> configs) throws SQLException {
        String sql = "UPDATE configs SET config_name = ?, first_left_color = ?, first_right_color = ?, second_left_color = ?, second_right_color = ?, third_left_color = ?, third_right_color = ?, player_piece_color = ?, enemy_piece_color = ?, screen_size_preferences = ?, effects_volume = ?, music_volume = ?, enabled_songs_flags = ? WHERE player_id = ? AND config_id = ?;";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int i = 0; i < configs.size(); i++) {
                Config current = configs.get(i);
                statement.setString(1, current.getConfigName());
                statement.setString(2, current.getFirstColorLeft());
                statement.setString(3, current.getFirstColorRight());
                statement.setString(4, current.getSecondColorLeft());
                statement.setString(5, current.getSecondColorRight());
                statement.setString(6, current.getThirdColorLeft());
                statement.setString(7, current.getThirdColorRight());
                statement.setString(8, current.getPlayerPieceColor());
                statement.setString(9, current.getEnemyPieceColor());
                statement.setString(10, current.getScreenSize());
                statement.setDouble(11, current.getSoundEffectsVolume());
                statement.setDouble(12, current.getMusicVolume());


                statement.setObject(13, current.getAvailableSongsFlags(), Types.OTHER);

                statement.setInt(14, playerId);
                statement.setInt(15, i + 1);
                statement.executeUpdate();
            }
            statement.close();
        } catch (SQLException ex) {
            throw new SQLException("Something went wrong while saving configs");
        }
    }

    private ArrayList<Integer> checkIfPlayerHasEnoughConfigs(int playerId) throws SQLException{
        ArrayList<Integer> existingConfigIds = new ArrayList<>();
        String sql = """
                SELECT config_id FROM configs WHERE player_id = ?;
                """;
        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, playerId);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                existingConfigIds.add(rs.getInt(1));
            }
            statement.close();
            return existingConfigIds;
        }catch (SQLException e) {
            throw new SQLException("Something went wrong while checking configs");
        }
    }

    private void addConfigsUntilFive(ArrayList<Integer> existingConfigIds, int playerId) throws SQLException{
        if(existingConfigIds.size() == 5) return;
        String sql = """
            INSERT INTO configs(config_id, player_id)
            VALUES(?, ?);
        """;
        try{
            PreparedStatement statement = connection.prepareStatement(sql);

            for(int i = 1; i<=5; i++){
                if(!existingConfigIds.contains(i)){
                    statement.setInt(1, i);
                    statement.setInt(2, playerId);
                    statement.executeUpdate();
                }
            }
        }catch (SQLException e) {
            throw new SQLException("Something went wrong while adding configs");
        }
    }
}
