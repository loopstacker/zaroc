package be.kdg.integration.gameapplication.model.databaseaccess;

import be.kdg.integration.gameapplication.model.gamematch.match.board.Move;
import be.kdg.integration.gameapplication.model.gamematch.match.board.MoveOwner;
import be.kdg.integration.gameapplication.model.gamematch.match.board.Position;
import be.kdg.integration.gameapplication.model.stats.GameData;
import be.kdg.integration.gameapplication.model.stats.GameStatus;
import org.postgresql.util.PGInterval;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Stack;

public class GameStatsDatabase {
    private Connection connection;

    public GameStatsDatabase(DataBaseConnection dataBaseConnection){
        this.connection = dataBaseConnection.getConnection();
    }

    public ArrayList<GameData> getGameData(int playerId) throws SQLException {
        try{
            ArrayList<GameData> gameData = new ArrayList<>();
            String sql = """
                SELECT game_id, game_status, game_start_date, reward, undo_left, player_points_before_game
                FROM games
                WHERE player_id = ?;
            """;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, playerId);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
               int gameId = resultSet.getInt(1);
               GameStatus gameStatus = GameStatus.valueOf(resultSet.getString(2).toUpperCase());
               Timestamp gameTimestamp = resultSet.getTimestamp(3);
               LocalDateTime localDateTime = gameTimestamp.toLocalDateTime();
               DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
               String gameDate = localDateTime.format(formatter);
               int gameReward = resultSet.getInt(4);
               int undoLeft = resultSet.getInt(5);
               int playerPointsBeforeGame = resultSet.getInt(6);
               gameData.add(new GameData(gameId, gameDate, gameStatus, gameReward, undoLeft, playerPointsBeforeGame));
            }
            resultSet.close();
            statement.close();
            return gameData;
        } catch (SQLException e) {
            throw new SQLException("Error retrieving the games data");
        }
    }

    public Stack<Move> loadAllMovesForGame(int gameId) throws SQLException{
        Stack<Move> allMoves = new Stack<>();
        String sql = """
            SELECT x_position_from, y_position_from, x_position_to, y_position_to, duration_of_move
            FROM moves
            WHERE game_id = ?
            ORDER BY move_id
        """;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, gameId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Position fromPositionXY = new Position(resultSet.getInt(1), resultSet.getInt(2));
                Position toPositionXY = new Position(resultSet.getInt(3), resultSet.getInt(4));
                PGInterval duration = (PGInterval) resultSet.getObject(5);
                float seconds = (float) duration.getMicroSeconds() / 1_000_000f
                        + (float) duration.getSeconds()
                        + (float) duration.getMinutes() * 60
                        + (float) duration.getHours() * 3600;

                allMoves.push(new Move(fromPositionXY, toPositionXY, MoveOwner.PLAYER, seconds));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e){
            throw new SQLException("Error loading moves for the game");
        }
        return allMoves;
    }
}
